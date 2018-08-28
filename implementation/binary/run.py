import argparse
import os.path
import xmltodict
import sys
import shutil
import glob
import zipfile
from collections import OrderedDict
from subprocess import check_output, CalledProcessError, run
from termcolor import colored

parser = argparse.ArgumentParser(description='Inserts the AFP lib into a target apk')
parser.add_argument('target_apk', type=str, help='the apk to instrument')

UNPACKED_FOLDER_SUFFIX = '-unpacked'
JARS_FOLDER_SUFFIX = '-jars'
CLASSES_FOLDER_SUFFIX = '-classes'
AFP_LIB_PACKAGE = 'gssi/aq/it/afplibrary'
AFP_LIB_FOLDER = 'libs/afplibrary'
COMMONS_LIB_FOLDER = 'libs/org/apache/commons/collections15'
MAPPINGS_FOLDER = 'mappings'
MAPPINGS_SUFFIX = '-mapping.txt'


# TODO: Readme -> requirements -> axmldec


def delete_file_or_dirs(directories_list: list) -> None:
    for temp in directories_list:
        if os.path.isdir(temp):
            print("- Removing temporary directory {}".format(temp))
            shutil.rmtree(temp)
        if os.path.isfile(temp):
            print("- Removing temporary file {}".format(temp))
            os.remove(temp)


def makedir_if_not_exists(dir_path: str) -> bool:
    created = False
    if not os.path.isdir(dir_path):
        print("Creating temporary directory {}".format(dir_path))
        os.makedirs(dir_path)
        created = True
    return created


def extract_main_activities(apk_path: str) -> list:
    main_activities = []
    print(colored("Extracting main activities from apk manifest file...", 'green'))
    manifest = xmltodict.parse(check_output(["axmldec", apk_path]).decode('utf-8'))
    for activity in manifest['manifest']['application']['activity']:
        if 'intent-filter' in activity:
            if isinstance(activity['intent-filter'], list):
                for elem in activity['intent-filter']:
                    if 'action' in elem:
                        if elem['action'].get('@android:name') == 'android.intent.action.MAIN':
                            main_activities.append(activity.get('@android:name'))
            if 'action' in activity['intent-filter']:
                for action in activity['intent-filter']['action']:
                    if isinstance(action, OrderedDict):
                        if action.get('@android:name') == 'android.intent.action.MAIN':
                            main_activities.append(activity.get('@android:name'))
                    elif isinstance(action, str):
                        if activity['intent-filter']['action'].get('@android:name') == 'android.intent.action.MAIN':
                            main_activities.append(activity.get('@android:name'))
    return main_activities


def unpack_apk(apk_path: str, apk_name: str) -> str:
    unpacked_folder = "{}{}".format(apk_name, UNPACKED_FOLDER_SUFFIX)
    print(colored("Unpacking apk file...", 'green'))
    # Create a temporary folder if it does not exist
    makedir_if_not_exists(unpacked_folder)
    # Run apktool to extract the sources
    check_output(["java", "-jar", "apktool.jar", "d", "-f", "-r", "-s", apk_path, "-o", unpacked_folder])
    return unpacked_folder


def decode_classes(unpacked_path: str, apk_name: str) -> str:
    jars_folder = "{}{}".format(apk_name, JARS_FOLDER_SUFFIX)
    print(colored("Decoding class files...", 'green'))
    # Create a temporary folder if it does not exist
    makedir_if_not_exists(jars_folder)
    # Find the class files
    dex_files = glob.glob('{}/*.dex'.format(unpacked_path))
    # Run dex2jar to decode classes
    for f in dex_files:
        print('Decoding {}'.format(f))
        jar_name, extension = os.path.splitext(os.path.basename(f))
        jar_path = os.path.join(jars_folder, jar_name + '.jar')
        check_output(["dex-tools-2.1/d2j-dex2jar.sh", '-f', "-o", jar_path, f])

    return jars_folder


def unzip_classes(jars_path: str, apk_name: str) -> str:
    classes_folder = "{}{}".format(apk_name, CLASSES_FOLDER_SUFFIX)
    print(colored("Unzipping class files...", 'green'))
    # Create a temporary folder if it does not exist
    makedir_if_not_exists(classes_folder)
    # Find the jar files
    jar_files = glob.glob('{}/*.jar'.format(jars_path))
    # Unzip the jar files
    for f in jar_files:
        print('Unzipping {}'.format(f))
        with zipfile.ZipFile(f, "r") as zip_ref:
            zip_ref.extractall(classes_folder)
    return classes_folder


def instrument_classes(app_name: str, classes_path: str, main_activities: list) -> None:
    afp_lib_path = os.path.join(classes_path, AFP_LIB_PACKAGE)
    mapping_path = os.path.join(MAPPINGS_FOLDER, app_name + MAPPINGS_SUFFIX)
    try:
        print(colored("Running AFP Instrumentor...", 'green'))
        print('Mappings file: {}'.format(mapping_path))
        # Run the instrumentor
        # TODO handling of multiple main activities
        run(["java", '-jar', "instrumenter/AFPinstrumenter.jar", classes_path, mapping_path, main_activities[0]])
        # Add the AFPLibrary classes
        print(colored("Adding AFP library...", 'green'))
        try:
            shutil.copytree(AFP_LIB_FOLDER, afp_lib_path)
        except FileExistsError:
            # TODO replace if file exists
            print("AFPLib already present, skipping...")
        try:
            shutil.copytree(COMMONS_LIB_FOLDER, os.path.join(classes_path, "org/apache/commons/collections15"))
        except FileExistsError:
            print("Apache Commons Lib already present, skipping...")
    except CalledProcessError:
        pass


def recompile_classes(classes_path: str) -> list:
    print(colored("Recompiling .dex file...", 'green'))
    dex_file_path = "."
    # TODO HANDLE --multi-dex
    check_output(["./dx", "--dex", "--multi-dex", "--output=" + dex_file_path, classes_path])
    return glob.glob("classe*.dex")


def rebuild_apk(unpacked_folder: str, dex_files: list, app_name: str) -> str:
    print(colored("Rebuilding APK file...", 'green'))
    apk_path = app_name + ".instrumented.apk"
    # Replace Classes.dex
    for f in dex_files:
        shutil.copy(f, unpacked_folder)
    # Run apktool to rebuild
    check_output(["java", "-jar", "apktool.jar", "b", "-f", unpacked_folder, "-o", apk_path])
    return apk_path


def sign_apk(modified_apk: str, app_name: str) -> str:
    print(colored("Signing APK file...", 'green'))
    apk_path = app_name + ".signed.apk"
    # Run dex2jar to sign the apk
    check_output(["dex-tools-2.1/d2j-apk-sign.sh", '-f', "-o", apk_path, modified_apk])
    return apk_path


def run_process(apk_path: str) -> None:
    apk_name, extension = os.path.splitext(os.path.basename(apk_path))
    # Extraction of main activities
    try:
        main_activities = extract_main_activities(apk_path)
    except CalledProcessError:
        print(colored("ERROR: could not find any main activity, terminating...", 'red'))
        sys.exit()
    print('Found the following main activities:')
    # Print main activities
    for a in main_activities:
        print('- {}'.format(a))
    # Extract the apk resources and classes
    try:
        unpacked_path = unpack_apk(apk_path, apk_name)
    except CalledProcessError:
        print(colored("ERROR: could not unpack the apk, terminating...", 'red'))
        sys.exit()
    # Decode the dex classes
    try:
        jars_path = decode_classes(unpacked_path, apk_name)
    except CalledProcessError:
        print(colored("ERROR: could not decode classes, terminating...", 'red'))
        delete_file_or_dirs([unpacked_path])
        sys.exit()
    # Unzip classes
    try:
        classes_path = unzip_classes(jars_path, apk_name)
    except FileNotFoundError:
        print(colored("ERROR: could not unzip classes, terminating...", 'red'))
        delete_file_or_dirs([unpacked_path, jars_path])
        sys.exit()
    # Run the instrumentor
    instrument_classes(apk_name, classes_path, main_activities)
    # Recompile classes
    try:
        dex_files = recompile_classes(classes_path)
    except CalledProcessError:
        print(colored("ERROR: could not recompile classes, terminating...", 'red'))
        # delete_file_or_dirs([unpacked_path, jars_path, classes_path])
        sys.exit()
    # Rebuild apk
    try:
        modified_apk = rebuild_apk(unpacked_path, dex_files, apk_name)
    except CalledProcessError:
        print(colored("ERROR: could not rebuild apk, terminating...", 'red'))
        delete_file_or_dirs([unpacked_path, jars_path, classes_path] + dex_files)
        sys.exit()
    # Sign the apk
    try:
        signed_apk = sign_apk(modified_apk, apk_name)
    except CalledProcessError:
        print(colored("ERROR: could not sign apk, terminating...", 'red'))
        delete_file_or_dirs([unpacked_path, jars_path, classes_path, modified_apk] + dex_files) 
        sys.exit()
    # Clean up
    print("- Output apk {}".format(signed_apk))
    print(colored("Cleaning up of temporary files...", 'green'))
    delete_file_or_dirs([unpacked_path, jars_path, modified_apk] + dex_files) #classes_path, 


if __name__ == '__main__':
    args = parser.parse_args()
    # Check the target apk exists
    if os.path.isfile(args.target_apk):
        run_process(args.target_apk)
    else:
        print(colored("ERROR: apk file does not exist, terminating...", 'red'))
