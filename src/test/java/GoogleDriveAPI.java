package test.java;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.mashape.unirest.http.exceptions.UnirestException;
import main.java.framework.Framework;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GoogleDriveAPI extends Framework {

    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static final java.io.File CREDENTIALS_FOLDER //
            = new java.io.File(System.getProperty("user.dir"), "resources");

    private static final String CLIENT_SECRET_FILE_NAME = "client_secret.json";

    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static Drive driveService;
    private static String fieldID;
    private static String folderId;

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        System.out.println("CREDENTIALS_FOLDER: " + CREDENTIALS_FOLDER.getAbsolutePath());
        java.io.File clientSecretFilePath = new java.io.File(CREDENTIALS_FOLDER, CLIENT_SECRET_FILE_NAME);
        if (!clientSecretFilePath.exists()) {
            throw new FileNotFoundException("Please copy " + CLIENT_SECRET_FILE_NAME //
                    + " to folder: " + CREDENTIALS_FOLDER.getAbsolutePath());
        }
        // Load client secrets.
        InputStream in = new FileInputStream(clientSecretFilePath);

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                clientSecrets, SCOPES).setDataStoreFactory(new FileDataStoreFactory(CREDENTIALS_FOLDER))
                .setAccessType("offline").build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    /**
     * Creating Driver object
     */
    public static Drive getDriveService() {
        try {
            if (driveService != null) {
                return driveService;
            }
            System.out.println("CREDENTIALS_FOLDER: " + CREDENTIALS_FOLDER.getAbsolutePath());

            if (!CREDENTIALS_FOLDER.exists()) {
                CREDENTIALS_FOLDER.mkdirs();
                System.out.println("Created Folder: " + CREDENTIALS_FOLDER.getAbsolutePath());
                System.out.println("Copy file " + CLIENT_SECRET_FILE_NAME + " into folder above.. and rerun this class!!");
                return null;
            }
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Credential credential = getCredentials(HTTP_TRANSPORT);
            driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential) //
                    .setApplicationName(APPLICATION_NAME).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return driveService;
    }

    @Test(priority = 0)
    public static void getFilesInGDrive() {
        Drive drive = getDriveService();
        // Print the names and IDs for up to 10 files.
        FileList result = null;
        try {
            result = drive.files().list().setFields("nextPageToken, files(id, name)").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                if(!file.getName().contains("."))
                    folderId=file.getId();
                System.out.println(file.getName() + " File with Id " + file.getId());
                reportPass("Verify the Google Drive files", " Files should be displayed  ", file.getName() + " file is displayed ");
            }
        }

    }

    public static void createFolderInGDrive(String folderIdParent, String folderName) throws IOException {
        File fileMetadata = new File();

        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        if (folderIdParent != null) {
            List<String> parents = Arrays.asList(folderIdParent);

            fileMetadata.setParents(parents);
        }
        Drive driveService = getDriveService();
        // Create a Folder.
        // Returns File object with id & name fields will be assigned values
        File file = driveService.files().create(fileMetadata).setFields("id, name,properties").execute();
        file.getProperties();

        driveService.about().get().getLastStatusCode();
        driveService.about().get().getLastStatusMessage();
        System.out.println("Created folder with id= " + file.getId());
        System.out.println("Created folder with name= " + file.getName());
    }

    @Test(priority = 1)
    @Parameters({"contentType", "fileName", "customFileName"})
    public static void uploadFileInGDrive(String contentType, String fileName, String customFileName) {
        java.io.File uploadFile = new java.io.File(System.getProperty("user.dir") + "//resources//" + fileName);
        AbstractInputStreamContent uploadStreamContent = new FileContent(contentType, uploadFile);
        File fileMetadata = new File();

        fileMetadata.setName(customFileName);
        fileMetadata.setParents(Collections.singletonList(folderId));

        Drive driveService = getDriveService();
        File file = null;
        try {
            file = driveService.files().create(fileMetadata, uploadStreamContent)
                    .setFields("id, webContentLink, webViewLink, parents").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fieldID = file.getId();
        reportPass("Verify the upload file", " Files should be uploaded  ", fileName + " file is uploaded link " + file.getWebViewLink());

        System.out.println("Created Google file!");
        System.out.println("WebContentLink: " + file.getWebContentLink());
        System.out.println("WebViewLink: " + file.getWebViewLink());
        System.out.println("Done!");
    }

    @Test(priority = 2)
    public static void downloadFilesInGDrive() throws IOException {
        Drive drive = getDriveService();
        //String fileId = "1ZdR3L3qP4Bkq8noWLJHSr_iBau0DNT4Kli4SxNc2YEo";
        OutputStream outputStream = new ByteArrayOutputStream();
        // drive.files().export(fileId, "application/pdf").executeMediaAndDownloadTo(outputStream);
        drive.files().get(fieldID)
                .executeMediaAndDownloadTo(outputStream);
        FileOutputStream fileOutputStream = new FileOutputStream(new java.io.File(System.getProperty("user.dir") + "//resources/downloads/image11.jpg"));
        ((ByteArrayOutputStream) outputStream).writeTo(fileOutputStream);
        reportPass("Verify the downloaded file ", " File should be downloaded ", "File downloaded successfully ");

    }
}