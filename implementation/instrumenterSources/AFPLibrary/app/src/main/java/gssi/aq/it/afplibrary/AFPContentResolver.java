package gssi.aq.it.afplibrary;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.PeriodicSync;
import android.content.SyncAdapterType;
import android.content.SyncInfo;
import android.content.SyncRequest;
import android.content.SyncStatusObserver;
import android.content.UriPermission;
import android.content.res.AssetFileDescriptor;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gianlucascoccia on 21/06/16.
 */
public class AFPContentResolver {

    protected ContentResolver wrappedResolver = null;

    //TODO si riesce a rendere la signature identica?
    public static AFPContentResolver getAFPContentResolver(Context context){
        return new AFPContentResolver(context);
    }

    public AFPContentResolver(Context context){
        // TODO Check
        wrappedResolver = context.getContentResolver();
    }

    public ContentProviderClient acquireContentProviderClient(Uri uri) {
        //TODO AFPContentProviderClient
        return null;
    }

    public final ContentProviderClient	acquireContentProviderClient(String name){
        //TODO AFPContentProviderClient
        return null;
    }

    public final ContentProviderClient	acquireUnstableContentProviderClient(String name){
        //TODO AFPContentProviderClient
        return null;
    }

    public final ContentProviderClient	acquireUnstableContentProviderClient(Uri uri){
        //TODO AFPContentProviderClient
        return null;
    }

    public final static void addPeriodicSync(Account account, String authority, Bundle extras, long pollFrequency) throws IllegalArgumentException {
        ContentResolver.addPeriodicSync(account, authority, extras, pollFrequency);
    }

    public final static Object addStatusChangeListener(int mask, SyncStatusObserver callback){
        return ContentResolver.addStatusChangeListener(mask, callback);
    }

    public final ContentProviderResult[]applyBatch(String authority, ArrayList<ContentProviderOperation> operations) throws OperationApplicationException, RemoteException {
        //TODO Check AFP model here
        return wrappedResolver.applyBatch(authority, operations);
    }

    public final int bulkInsert(Uri url, ContentValues[] values) {
        //TODO Check AFP model here
        return wrappedResolver.bulkInsert(url, values);
    }

    public final Bundle	call(Uri uri, String method, String arg, Bundle extras) {
        //TODO Check AFP model here
        return wrappedResolver.call(uri, method, arg, extras);
    }

    public final void	cancelSync(Uri uri){
        wrappedResolver.cancelSync(uri);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public final static void cancelSync(SyncRequest request) {
        ContentResolver.cancelSync(request);
    }

    public final static void cancelSync(Account account, String authority){
        ContentResolver.cancelSync(account, authority);
    }

    public final Uri canonicalize(Uri url){
        return wrappedResolver.canonicalize(url);
    }

    public final int delete(Uri url, String where, String[] selectionArgs){
        //TODO Check AFP model here
        return wrappedResolver.delete(url, where, selectionArgs);
    }

    public final static SyncInfo getCurrentSync() {
        return ContentResolver.getCurrentSync();
    }

    public final static List<SyncInfo> getCurrentSyncs(){
        return ContentResolver.getCurrentSyncs();
    }

    public final static int getIsSyncable(Account account, String authority){
        return ContentResolver.getIsSyncable(account, authority);
    }

    public final static boolean getMasterSyncAutomatically(){
        return ContentResolver.getMasterSyncAutomatically();
    }

    public final List<UriPermission> getOutgoingPersistedUriPermissions(){
        return wrappedResolver.getOutgoingPersistedUriPermissions();
    }

    public final static List<PeriodicSync> getPeriodicSyncs(Account account, String authority){
        return ContentResolver.getPeriodicSyncs(account, authority);
    }

    public final List<UriPermission>	getPersistedUriPermissions() {
        return wrappedResolver.getPersistedUriPermissions();
    }

    public final String[]	getStreamTypes(Uri url, String mimeTypeFilter) {
        return wrappedResolver.getStreamTypes(url, mimeTypeFilter);
    }

    public final static SyncAdapterType[]	getSyncAdapterTypes() {
        return ContentResolver.getSyncAdapterTypes();
    }

    public final static boolean getSyncAutomatically(Account account, String authority){
        return ContentResolver.getSyncAutomatically(account, authority);
    }

    public final String	getType(Uri url) {
        return wrappedResolver.getType(url);
    }

    public final Uri insert(Uri url, ContentValues values){
        //TODO Check AFP model here
        return wrappedResolver.insert(url, values);
    }

    public final static boolean isSyncActive(Account account, String authority){
        return ContentResolver.isSyncActive(account, authority);
    }

    public final static boolean isSyncPending(Account account, String authority){
        return ContentResolver.isSyncActive(account, authority);
    }

    public final void	notifyChange(Uri uri, ContentObserver observer, boolean syncToNetwork){
        wrappedResolver.notifyChange(uri, observer, syncToNetwork);
    }

//    public void	notifyChange(Uri uri, ContentObserver observer, int flags) {
//        wrappedResolver.notifyChange(uri, observer, flags);
//    }

    public final void	notifyChange(Uri uri, ContentObserver observer) {
        wrappedResolver.notifyChange(uri, observer);
    }

    public final AssetFileDescriptor openAssetFileDescriptor(Uri uri, String mode, CancellationSignal cancellationSignal) throws FileNotFoundException {
        //TODO Check AFP model here??
        return wrappedResolver.openAssetFileDescriptor(uri, mode, cancellationSignal);
    }

    public final AssetFileDescriptor openAssetFileDescriptor(Uri uri, String mode) throws FileNotFoundException {
        //TODO Check AFP model here??
        return wrappedResolver.openAssetFileDescriptor(uri, mode);
    }

    public final ParcelFileDescriptor openFileDescriptor(Uri uri, String mode, CancellationSignal cancellationSignal) throws FileNotFoundException {
        //TODO Check AFP model here??
        return wrappedResolver.openFileDescriptor(uri, mode, cancellationSignal);
    }

    public final ParcelFileDescriptor openFileDescriptor(Uri uri, String mode) throws FileNotFoundException {
        //TODO Check AFP model here??
        return openFileDescriptor(uri, mode);
    }

    public final InputStream openInputStream(Uri uri) throws FileNotFoundException {
        //TODO Check AFP model here
        return wrappedResolver.openInputStream(uri);
    }

    public final OutputStream openOutputStream(Uri uri) throws FileNotFoundException {
        //TODO Check AFP model here
        return wrappedResolver.openOutputStream(uri);
    }

    public final OutputStream openOutputStream(Uri uri, String mode) throws FileNotFoundException {
        //TODO Check AFP model here
        return wrappedResolver.openOutputStream(uri, mode);
    }

    public final AssetFileDescriptor openTypedAssetFileDescriptor(Uri uri, String mimeType, Bundle opts, CancellationSignal cancellationSignal) throws FileNotFoundException {
        //TODO Check AFP model here??
        return wrappedResolver.openTypedAssetFileDescriptor(uri, mimeType, opts, cancellationSignal);
    }

    public final AssetFileDescriptor openTypedAssetFileDescriptor(Uri uri, String mimeType, Bundle opts) throws FileNotFoundException {
        //TODO Check AFP model here??
        return wrappedResolver.openTypedAssetFileDescriptor(uri, mimeType, opts);
    }

    public final Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, CancellationSignal cancellationSignal) {
        //TODO Check AFP model here
         return wrappedResolver.query(uri, projection, selection, selectionArgs, sortOrder, cancellationSignal);
    }

    public final Cursor	query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //TODO Check AFP model here
        return wrappedResolver.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    public final void registerContentObserver(Uri uri, boolean notifyForDescendants, ContentObserver observer){
        //TODO Check AFP model here??
        wrappedResolver.registerContentObserver(uri, notifyForDescendants, observer);
    }

    public final void	releasePersistableUriPermission(Uri uri, int modeFlags) {
        wrappedResolver.releasePersistableUriPermission(uri, modeFlags);
    }

    public final static void removePeriodicSync(Account account, String authority, Bundle extras) {
        ContentResolver.removePeriodicSync(account, authority, extras);
    }

    public final static void removeStatusChangeListener(Object handle) {
        ContentResolver.removeStatusChangeListener(handle);
    }

    public final static void requestSync(Account account, String authority, Bundle extras) {
        ContentResolver.requestSync(account, authority, extras);
    }

    public final static void requestSync(SyncRequest request) {
        ContentResolver.requestSync(request);
    }

    public final static void setIsSyncable(Account account, String authority, int syncable) {
        ContentResolver.setIsSyncable(account, authority, syncable);
    }

    public final static void setMasterSyncAutomatically(boolean sync) {
        ContentResolver.setMasterSyncAutomatically(sync);
    }

    public final static void setSyncAutomatically(Account account, String authority, boolean sync) {
        ContentResolver.setSyncAutomatically(account, authority, sync);
    }

    public final void	startSync(Uri uri, Bundle extras) {
        wrappedResolver.startSync(uri, extras);
    }

    public final void	takePersistableUriPermission(Uri uri, int modeFlags) {
        wrappedResolver.takePersistableUriPermission(uri, modeFlags);
    }

    public final Uri uncanonicalize(Uri url) {
        return wrappedResolver.uncanonicalize(url);
    }

    public final void unregisterContentObserver(ContentObserver observer) {
        wrappedResolver.unregisterContentObserver(observer);
    }

    public final int update(Uri uri, ContentValues values, String where, String[] selectionArgs) {
        return wrappedResolver.update(uri, values, where, selectionArgs);
    }

    public final static void	validateSyncExtrasBundle(Bundle extras) {
        ContentResolver.validateSyncExtrasBundle(extras);
    }

}
