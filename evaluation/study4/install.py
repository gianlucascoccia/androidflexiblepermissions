# Script to automate the apps installation process

import subprocess
import re
from argparse import ArgumentParser
from termcolor import colored


def examine_path(apk_path: str) -> None:
    package_name = get_package_name(apk_path)
    if check_is_installed(package_name):
        print(colored('* Uninstalling the already installed app', 'green'))
        remove_app(package_name)
    if '.signed' in apk_path:
        print(colored('* Installing instrumented app', 'green'))
        install_app(apk_path)
        print(colored('* Granting permissions', 'green'))
        grant_permissions(package_name)
    else:
        print(colored('* Installing base app', 'green'))
        install_app(apk_path)


def get_package_name(apk_path: str) -> str:
    out = subprocess.check_output('aapt dump badging {}'.format(apk_path), shell=True)
    m = re.search('name=\'([a-z.]*)\'', str(out))
    return m.group(1)


def install_app(apk_path: str) -> None:
    subprocess.run('adb install {}'.format(apk_path), shell=True)


def check_is_installed(package_name: str) -> bool:
    out = subprocess.check_output('adb shell pm list packages', shell=True)
    return True if package_name in str(out) else False


def remove_app(package_name: str) -> None:
    subprocess.run('adb uninstall {}'.format(package_name), shell=True)


def grant_permissions(package_name: str) -> None:
    subprocess.run('adb shell pm grant {} android.permission.ACCESS_COARSE_LOCATION'.format(package_name), shell=True)
    subprocess.run('adb shell pm grant {} android.permission.ACCESS_FINE_LOCATION'.format(package_name), shell=True)
    subprocess.run('adb shell pm grant {} android.permission.READ_EXTERNAL_STORAGE'.format(package_name), shell=True)
    subprocess.run('adb shell pm grant {} android.permission.WRITE_CALENDAR'.format(package_name), shell=True)
    subprocess.run('adb shell pm grant {} android.permission.CAMERA'.format(package_name), shell=True)
    subprocess.run('adb shell pm grant {} android.permission.READ_CONTACTS'.format(package_name), shell=True)
    subprocess.run('adb shell pm grant {} android.permission.GET_ACCOUNTS'.format(package_name), shell=True)


parser = ArgumentParser()
parser.add_argument('-a', '--apk', dest='apk', help='Apk to be installed', metavar='APK', required=True)
args = parser.parse_args()
examine_path(vars(args)['apk'])

