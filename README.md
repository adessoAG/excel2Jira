# excel2Jira

[![Build Status](https://travis-ci.org/adessoAG/excel2Jira.svg?branch=master)](https://travis-ci.org/adessoAG/excel2Jira)

The tool reads issues from an Excel spreadsheet and posts them to JIRA.

### Usage

`java -jar excel2jira.jar --file="filename.xlsx" --username="jira_username" --url="jira.server.domain" --fixVersions="version1, version2" 
--labels="label1, label2"`

After starting the jar with the given arguments, the user is prompted to enter their password.
Any errors will be visible in the log.

`java -jar excel2jira.jar help` will print a short message detailing usage.

### Notes

The tool uses HTTP Basic Authentication to authenticate to JIRA. This means that the entered password is
only Base64 encoded and not in any way encrypted. For this reason the connection to the server is only allowed to happen over HTTPS.

Also, some fields (such as labels) must be configured in the edit screen for your project. Otherwise an error will be thrown or the field
will be ignored.
