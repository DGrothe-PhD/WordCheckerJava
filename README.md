# WordCheckerJava
Counts word occurrences in a text file. Results are saved in alphabetical order in a simple HTML file. The application opens a GUI window.

## How it works
Numbers and bracketed numbers (often reference signs) are listed separately.

With regular expressions, simple filtering for data types similar to ISBN, date or time, URLs or e-mail addresses is done; these tokens are listed separately as well. As regular expressions can be fiddly, I focused on a simple and stable solution so this filtering may not always yield perfect results.

## Usage
`Open File`: browse to a text file. Set the topic (this will be the HTML title) and the target filename. The ending `.html` will be added automatically if missing. Target file will be placed in the same folder as the java program (folder choice button to be implemented later).

Then click on `Start`. Words from the text file are gathered, collected and sorted. Finally, click the button `Show Results` to open the result file in a browser.

## Browser and editor support
Basically, the results.html file can be opened with any later browser, e.g. Firefox and Microsoft Edge.
As clickable `<details>` tags are used, Internet Explorer is not fully supported. The html source code itself is kept readable so that compatibility issues are minimized. 

This way, quick review of an entry by command line tools as well: such as
(Linux only) `less results.html | grep <word>` or (Windows command line) `type results.html | findstr "1"`. Any suitable editor tool works as well.

## Requirements
To compile, a JDK is required, it will run on a JRE 8 or later.

## Setup
TBD