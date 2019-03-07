Batch Extractor v2.2

Extracts zip files or folders with zip files containing java or cpp and header files.
It places the extracted contents in a new folder with the suffix " Unzipped".

Additional options:
Moss only - only unzips files containing the suffix "moss.zip" (case insensitive)
Shorten names - removes the blackboard generated portion of the name for folders containing individual student's files
  Example:
  Original - AssignmentName_Username_Attempt_Date_LastNameFirstName_AssignmentName_Moss
  Renamed - LastNameFirstName_AssignmentName_Moss
Blackboard  Zip - performs an initial unzip on the zip file provided by blackboard containing all of the other student's zip files into a temporary folder for the program to work with

Future plans:
Major rewrite of the logic to allow for greater flexibility. Ideally this program wouldn't be specific to just extracting zip files for blackboard, but could be used extracting anything that requires specific formatting. Things like user defined whitelists, blacklists, and operations performed after the extraction, like moving and renaming of files, would greatly help add flexibility while also maintaining the ability to perform it's initial purpose. In order to do this, a custom interpreter would have to be developed to be able to allow users to use predefined variables, like %FilePath% or %UserHome%, and to be able to use string modifying expressions like substring, as well as be able to allow special characters, for example *.java to signify any file with the java file extension. This would allow for things like the whitelists and blacklists to not be limited only to static absolute file paths.
The unzipping function itself should also be modified to allow for recurisvely unzipping nested zip files, as well as allow for the unzipping of different archive types like 7zip and rar which would either be classes that implement a common interface or a child of a super class depending on how differently the unzipping process is for the different archives.
