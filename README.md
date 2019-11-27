# FileHandling
A collection of reusable actions concerning 'Files'.

## Community Commons series 
Community Commons is a series of modules for and by the community extending the low-code capabilities of the Mendix Platform.

## Microflow Activities (Java actions exposed as microflows)
- Base64 - Create encoded string from FileDocument contents
- Base64 - Decode Base64 string and store as FileDocument contents
- FileDocument - Create a local file from FileDocument contents
- FileDocument - Duplicate contents
- FileDocument - Get FileDocument contents as string
- FileDocument - Get FileDocument contents file size (Bytes)
- FileDocument - Store contents from URL in FileDocument object
- FileDocument - Store contents from file (located in resources folder)
- FileDocument - Store local file content in FileDocument object
- FileDocument - Write string to FileDocument object
- Image - Duplicate image contents
- Image - Get image dimensions

## Dependencies
- ~~commons-codec-1.10.jar~~
- ~~org.apache.commons.fileupload-1.2.1.jar~~
- commons-io-2.6.jar

## Contributing
The source of this project is hosted on Github.
GitHub is a code hosting platform for version control and collaboration. It lets you and others work together on projects from anywhere.

For more information on how to contribute to this repository visit [Contributing to a GitHub repository](https://docs.mendix.com/howto/collaboration-requirements-management/contribute-to-a-github-repository)!

### Gradle 
In version 1.3.0, we introduce dependency management using a [Gradle](https://gradle.org/install/) build file.
The Gradle build file makes sure all required dependencies are copied into the `userlib` folder of the project.
To download the dependencies and copy them to the `userlib/` folder, execute the following command:
```
gradle prepareDeps
``` 
from the command line. 

Unfortunately, this doesn't mean that obsoleted jars are automatically deleted from your projects' `userlib` folder when you import the Community Commons module into your app model.

### Exporting the module
Only select the files from the userlib directory which have an equivalent file with the `Filehandling.RequiredLib` extension.
