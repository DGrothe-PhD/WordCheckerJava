# WordCheckerJava
A Java UI which counts word occurrences in a text file. Results are sorted in alphabetical order and saved in a simple HTML file. With this, you can:
* Search for multiple terms in one go.
* Quickly find misspelt words in large texts
* Sneak into the context around some keywords.

## How it works
Numbers and bracketed numbers (often reference signs) are listed separately.
User can uncheck words, symbols, numbers or custom search terms, thereby shortening the result html file. Scenarios:
Unchecking symbols can be useful if a source text contains some mathematical expressions, for example. Unchecking numbers may be helpful if a text contains line numbers.

With regular expressions, simple filtering for data types similar to ISBN, date or time, URLs or e-mail addresses is done; these tokens are listed separately as well. As regular expressions can be fiddly, I focused on a simple and stable solution so this filtering may not always yield perfect results.

## Use cases
* Filter ISBNs, e-mail addresses or URLs that occur in a long e-mail or messenger thread.
* Have all occurrences of a word or a name in a long thesis been spelt in the same manner?
* Does a text contain a specific search term?<br>
 If so, in which context, that is, within which sentence or paragraph, does the search term appear?<br>
 The size of the shown text around that search term can be specified by the user.

## Usage
`Open File`: browse to a text file. Set the topic (this will be the HTML title) and the target filename. The ending `.html` will be added automatically if missing. Target file will be placed in the same folder as the source text file (folder choice button may be implemented later).

Then click on `Start`. Words from the text file are gathered, collected and sorted. Finally, click the button `Show Results` to open the result file in a browser.

## Browser and editor support
Basically, the results.html file can be opened with any later browser, e.g. Firefox and Microsoft Edge.
As clickable `<details>` tags are used, Internet Explorer is not fully supported. The html source code itself is kept readable so that compatibility issues are minimized. 

This way, quick review of an entry by command line tools as well: such as
(Linux only) `less results.html | grep <word>` or (Windows command line) `type results.html | findstr "1"`. Any suitable editor tool works as well.

## Requirements
To compile, a JDK is required, it will run on a JRE 8 or later.<br>
The jar file can then be used on different platforms including a Raspberry Pi (it has been tested on Raspberry Pi3).

<img src="./examples/GUIWindowDE.png" width="30%" title="German GUI window" alt="German GUI window"/>&nbsp;
<img src="./examples/GUIWindowEN.png" width="30%" title="Default GUI window" alt="Default GUI window"/>&nbsp;
<img src="./examples/GUIWarningFR.png" width="30%" title="Example French GUI window" alt="Example French GUI window"/>
