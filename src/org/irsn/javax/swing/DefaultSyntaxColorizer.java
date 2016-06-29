package org.irsn.javax.swing;

import java.awt.*;
import java.util.*;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;

/**This DocumentFilter supports syntax dependant colorization. RegExp are available if RegExpHashMap is used to give KeyWords
 * Yous should consider to simplify this class for better performance.
 */
public class DefaultSyntaxColorizer extends SyntaxColorizer {

    protected StyledDocument doc;
    private Element rootElement;
    private boolean multiLineComment;
    private MutableAttributeSet normal;
    private MutableAttributeSet comment;
    private MutableAttributeSet quote;
    private MutableAttributeSet operator;
    private MutableAttributeSet numbers;
    protected HashMap<String, Color> keywords;
    protected HashMap<Color, MutableAttributeSet> colors;
    UndoableEditListener undo;
    JXTextPane component;

    public DefaultSyntaxColorizer(JXTextPane component, HashMap<String, Color> keywords) {
        this.component = component;
        this.doc = component.getStyledDocument();
        this.undo = component.getUndoableEditListener();

        rootElement = doc.getDefaultRootElement();
        doc.putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");

        normal = new SimpleAttributeSet();
        StyleConstants.setForeground(normal, Color.black);

        comment = new SimpleAttributeSet();
        StyleConstants.setForeground(comment, Color.gray);
        StyleConstants.setItalic(comment, true);

        quote = new SimpleAttributeSet();
        StyleConstants.setForeground(quote, Color.orange);

        operator = new SimpleAttributeSet();
        StyleConstants.setForeground(operator, Color.blue);

        numbers = new SimpleAttributeSet();
        StyleConstants.setForeground(numbers, Color.red);

        setKeywordColor(keywords);
    }

//TODO setKeywordBackgroundColor, setKeywordItalic, setKeywordBold, setKeywordUnderline
    public void setKeywordColor(HashMap<String, Color> keywords) {
        colors = null;
        colors = new HashMap<Color, MutableAttributeSet>();
        this.keywords = keywords;

        if (keywords == null) {
            return;
        }
        if (keywords instanceof RegExpHashMap) {
            ((RegExpHashMap) keywords).keyAsRegexp = false;
        }

        for (String k : keywords.keySet()) {
            if (!colors.containsKey(keywords.get(k))) {
                MutableAttributeSet kw = new SimpleAttributeSet();
                StyleConstants.setForeground(kw, keywords.get(k));
                colors.put(keywords.get(k), kw);
            }
        }

        if (keywords instanceof RegExpHashMap) {
            ((RegExpHashMap) keywords).keyAsRegexp = true;
        }
    }

    /** Here is a way to handle regexp on keywords.*/
    public static class RegExpHashMap extends HashMap {

        public boolean keyAsRegexp = true;

        @Override
        public boolean containsKey(Object o) {
            if (keyAsRegexp) {
                for (Object regexp_key : super.keySet()) {
                    if (o.toString().matches(regexp_key.toString())) {
                        return true;
                    }
                }
                return false;
            } else {
                for (Object regexp_key : super.keySet()) {
                    if (o.toString().equals(regexp_key.toString())) {
                        return true;
                    }
                }
                return false;
            }
        }

        @Override
        public Object get(Object o) {
            if (keyAsRegexp) {
                for (Object regexp_key : super.keySet()) {
                    if (o.toString().matches(regexp_key.toString())) {
                        Object found = super.get(regexp_key.toString());
                        return found;
                    }
                }
                return null;
            } else {
                for (Object regexp_key : super.keySet()) {
                    if (o.toString().equals(regexp_key.toString())) {
                        Object found = super.get(regexp_key.toString());
                        return found;
                    }
                }
                return null;
            }
        }
    }
    /*
     *  Override to apply syntax highlighting after the document has been updated
     */

    @Override
    public void insertString(DocumentFilter.FilterBypass b, int offset, String str, AttributeSet a) throws BadLocationException {
        b.insertString(offset, str, a);
        if (undo != null) {
            doc.removeUndoableEditListener(undo);
            processChangedLines(offset, str.length());
            doc.addUndoableEditListener(undo);
        } else {
            processChangedLines(offset, str.length());
        }
    }

    /*
     *  Override to apply syntax highlighting after the document has been updated
     */
    @Override
    public void remove(DocumentFilter.FilterBypass b, int offset, int length) throws BadLocationException {
        b.remove(offset, length);
        if (undo != null) {
            doc.removeUndoableEditListener(undo);
            processChangedLines(offset, 0);
            doc.addUndoableEditListener(undo);
        } else {
            processChangedLines(offset, 0);
        }
    }

    @Override
    public void replace(DocumentFilter.FilterBypass b, int offset, int length, String str, AttributeSet as) throws BadLocationException {
        b.replace(offset, length, str, as);
        if (undo != null) {
            doc.removeUndoableEditListener(undo);
            processChangedLines(offset, str.length());
            doc.addUndoableEditListener(undo);
        } else {
            processChangedLines(offset, str.length());
        }
    }

    /*
     *  Determine how many lines have been changed,
     *  then apply highlighting to each line
     */
    public void processChangedLines(int offset, int length)
            throws BadLocationException {
        String content = doc.getText(0, doc.getLength());

        //  The lines affected by the latest document update

        int startLine = rootElement.getElementIndex(offset);
        int endLine = rootElement.getElementIndex(offset + length);

        //  Make sure all comment lines prior to the start line are commented
        //  and determine if the start line is still in a multi line comment

        setMultiLineComment(commentLinesBefore(content, startLine));

        //  Do the actual highlighting

        for (int i = startLine; i <= endLine; i++) {
            applyHighlighting(content, i);
        }

        //  Resolve highlighting to the next end multi line delimiter

        if (isMultiLineComment()) {
            commentLinesAfter(content, endLine);
        } else {
            highlightLinesAfter(content, endLine);
        }
    }

    /*
     *  Highlight lines when a multi line comment is still 'open'
     *  (ie. matching end delimiter has not yet been encountered)
     */
    private boolean commentLinesBefore(String content, int line) {
        int offset = rootElement.getElement(line).getStartOffset();

        //  Start of comment not found, nothing to do

        int startDelimiter = content.lastIndexOf(getStartDelimiter(), offset);
        if (startDelimiter < 0) {
            return false;
        }

        //  Matching start/end of comment found, nothing to do

        int endDelimiter = content.indexOf(getEndDelimiter(), startDelimiter);
        if (endDelimiter != -1 && endDelimiter < offset) {
            return false;
        }

        //  End of comment not found, highlight the lines

        doc.setCharacterAttributes(startDelimiter, offset - startDelimiter + 1, comment, false);
        return true;
    }

    /*
     *  Highlight comment lines to matching end delimiter
     */
    private void commentLinesAfter(String content, int line) {
        int offset = rootElement.getElement(line).getEndOffset();

        //  End of comment not found, nothing to do
        int endDelimiter = content.indexOf(getEndDelimiter(), offset);
        if (endDelimiter < 0) {
            doc.setCharacterAttributes(offset, content.length() - offset + getEndDelimiter().length(), comment, false);
            return;
        }

        doc.setCharacterAttributes(offset, endDelimiter - offset + getEndDelimiter().length(), comment, false);
    }

    /*
     *  Highlight lines to start or end delimiter
     */
    private void highlightLinesAfter(String content, int line)
            throws BadLocationException {
        int offset = rootElement.getElement(line).getEndOffset();

        int startDelimiter = content.indexOf(getStartDelimiter(), offset);
        int endDelimiter = content.indexOf(getEndDelimiter(), offset);

        if (startDelimiter < 0) {
            startDelimiter = content.length();
        }

        if (endDelimiter < 0) {
            endDelimiter = content.length();
        }

        int delimiter = Math.min(startDelimiter, endDelimiter);
        if (delimiter < offset) {
            return;
        }

        //	Start/End delimiter found, reapply highlighting

        int endLine = rootElement.getElementIndex(delimiter);

        for (int i = line + 1; i <= endLine; i++) {
            Element branch = rootElement.getElement(i);
            Element leaf = doc.getCharacterElement(branch.getStartOffset());
            AttributeSet as = leaf.getAttributes();

            if (as.isEqual(comment)) {
                applyHighlighting(content, i);
            }
        }
    }

    /*
     *  Parse the line to determine the appropriate highlighting
     */
    private void applyHighlighting(String content, int line)
            throws BadLocationException {
        int startOffset = rootElement.getElement(line).getStartOffset();
        int endOffset = rootElement.getElement(line).getEndOffset() - 1;

        int lineLength = endOffset - startOffset;
        int contentLength = content.length();

        if (endOffset >= contentLength) {
            endOffset = contentLength - 1;
        }

        doc.setCharacterAttributes(startOffset, lineLength, normal, false);

        //  check for multi line comments
        int e = endOffset;
        int s = startOffset;
        if (isMultiLineComment()) {
            if ((e = endingMultiLineComment(content, startOffset, endOffset)) > -1) {
                doc.setCharacterAttributes(startOffset, e - startOffset + getEndDelimiter().length(), comment, false);
                setMultiLineComment(false);
                if ((s = startingMultiLineComment(content, startOffset, endOffset)) > -1 && s > e) {
                    doc.setCharacterAttributes(s, endOffset - s + 1, comment, false);
                    setMultiLineComment(true);
                    checkForTokens(content, e + getEndDelimiter().length(), s);
                    return;
                }
                checkForTokens(content, e + getEndDelimiter().length(), endOffset);
                return;
            } else {
                doc.setCharacterAttributes(startOffset, endOffset - startOffset + 1, comment, false);
                setMultiLineComment(true);
                return;
            }
        } else if ((s = startingMultiLineComment(content, startOffset, endOffset)) > -1) {
            if (((e = endingMultiLineComment(content, startOffset, endOffset)) > -1) && e > s) {
                doc.setCharacterAttributes(s, e - s + getEndDelimiter().length(), comment, false);
                setMultiLineComment(false);
                checkForTokens(content, startOffset, s-1);
                checkForTokens(content, e + getEndDelimiter().length(), endOffset);
                return;
            } else {
                doc.setCharacterAttributes(s, endOffset - s + 1, comment, false);
                checkForTokens(content, startOffset, s-1);
                setMultiLineComment(true);
                return;
            }
        }

        //  check for single line comment
        int index = content.indexOf(getSingleLineDelimiter(), startOffset);
        if ((index > -1) && (index < endOffset)) {
            doc.setCharacterAttributes(index, endOffset - index + 1, comment, false);
            endOffset = index - 1;
        }

        //  check for tokens
        checkForTokens(content, startOffset, endOffset);
    }

    /*
     *  Does this line contain the start delimiter
     */
    private int startingMultiLineComment(String content, int startOffset, int endOffset)
            throws BadLocationException {
        int index = content.indexOf(getStartDelimiter(), startOffset);

        if ((index < 0) || (index > endOffset)) {
            return -1;
        } else {
            return index;
        }
    }

    /*
     *  Does this line contain the end delimiter
     */
    private int endingMultiLineComment(String content, int startOffset, int endOffset)
            throws BadLocationException {
        int index = content.indexOf(getEndDelimiter(), startOffset);

        if ((index < 0) || (index > endOffset)) {
            return -1;
        } else {
            return index;
        }
    }

    /*
     *  We have found a start delimiter
     *  and are still searching for the end delimiter
     */
    private boolean isMultiLineComment() {
        return multiLineComment;
    }

    private void setMultiLineComment(boolean value) {
        multiLineComment = value;
    }

    /*
     *	Parse the line for tokens to highlight
     */
    protected void checkForTokens(String content, int startOffset, int endOffset) {
        while (startOffset <= endOffset) {
            //  skip the delimiters to find the start of a new token

            while (isWhiteSpace(content.charAt(startOffset))) {
                if (startOffset < endOffset) {
                    startOffset++;
                } else {
                    return;
                }
            }

            //  Extract and process the entire token
            if (isQuoteDelimiter(content.charAt(startOffset))) {
                startOffset = getQuoteToken(content, startOffset, endOffset);
            } else {
                startOffset = getOtherToken(content, startOffset, endOffset);
            }
        }
    }

    /*
     *
     */
    protected int getQuoteToken(String content, int startOffset, int endOffset) {
        String quoteDelimiter = content.substring(startOffset, startOffset + 1);
        String escapeString = getEscapeString(quoteDelimiter);

        int index;
        int endOfQuote = startOffset;

        //  skip over the escape quotes in this quote

        index = content.indexOf(escapeString, endOfQuote + 1);

        while ((index > -1) && (index < endOffset)) {
            endOfQuote = index + 1;
            index = content.indexOf(escapeString, endOfQuote);
        }

        // now find the matching delimiter

        index = content.indexOf(quoteDelimiter, endOfQuote + 1);

        if ((index < 0) || (index > endOffset)) {
            endOfQuote = endOffset;
        } else {
            endOfQuote = index;
        }

        doc.setCharacterAttributes(startOffset, endOfQuote - startOffset + 1, quote, false);

        return endOfQuote + 1;
    }

    protected int getOtherToken(String content, int startOffset, int endOffset) {
        if (isStartNumber(content.charAt(startOffset)) && (startOffset == 0 || !(isLetter(content.charAt(startOffset - 1)) || isNumber(content.charAt(startOffset - 1))))) {//to avoid sdfgdfg<.>654654
            if (endOffset > startOffset + 1) {
                if (isNumber(content.charAt(startOffset + 1))) {//to avoid 65465454<.>rsdgdrg
                    int endOfToken = startOffset + 1;
                    while (endOfToken < endOffset && isNumber(content.charAt(endOfToken))) {
                        endOfToken++;
                    }
                    if (endOfToken == endOffset || !isLetter(content.charAt(endOfToken))) {// to avoid 65465<4>zertert
                        doc.setCharacterAttributes(startOffset, endOfToken - startOffset + 1, numbers, false);
                        return endOfToken;
                    }
                } else {
                    if (isDigit(content.charAt(startOffset))) {
                        doc.setCharacterAttributes(startOffset, 1, numbers, false);
                        return startOffset + 1;
                    }
                }
            } else {
                if (isDigit(content.charAt(startOffset))) {
                    doc.setCharacterAttributes(startOffset, 1, numbers, false);
                    return startOffset + 1;
                }
            }
        }

        if (isOperator(content.charAt(startOffset))) {
            doc.setCharacterAttributes(startOffset, 1, operator, false);
            return startOffset + 1;
        }

        int endOfToken = startOffset + 1;
        while (endOfToken <= endOffset) {// to reach end of token
            if (isTokenSeparator(content.charAt(endOfToken))) {
                break;
            }

            endOfToken++;
        }

        String token = content.substring(startOffset, endOfToken).toUpperCase(); // Edited by Skyost (adding toUpperCase() = ignoring case).

        if (isKeyword(token)) {
            doc.setCharacterAttributes(startOffset, endOfToken - startOffset, colors.get(keywords.get(token)), false);
        }

        return endOfToken;
    }

    /*
     *  Override for other languages
     */
    public boolean isTokenSeparator(char character) {
        return isWhiteSpace(character) || isOperator(character);
    }

    public boolean isWhiteSpace(char character) {
        return Character.isWhitespace(character);
    }

    public boolean isLetter(char character) {
        if (Character.isLetter(character) || character == '_') {
            return true;
        } else {
            return false;
        }
    }

    public boolean isStartNumber(char c) {
        return isDigit(c) || c == '-' || c == '+' || c == '.';
    }

    public boolean isNumber(char c) {
        return isDigit(c) || c == '.' /*|| c == 'E'*/;
    }

    public boolean isOperator(char character) {
        for (int i = 0; i < getOperands().length(); i++) {
            if (character == getOperands().charAt(i)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDigit(char c) {
        return Character.isDigit(c);
    }

    /*
     *  Override for other languages
     */
    public boolean isQuoteDelimiter(char character) {
        for (int i = 0; i < getQuotes().length(); i++) {
            if (character == getQuotes().charAt(i)) {
                return true;
            }
        }
        return false;
    }

    /*
     *  Override for other languages
     */
    public boolean isKeyword(String token) {
        //return keywords.contains(token);
        if (keywords == null) {
            return false;
        }
        return keywords.containsKey(token);
    }

    /*
     *  Override for other languages
     */
    public String getStartDelimiter() {
        return "/*";
    }

    /*
     *  Override for other languages
     */
    public String getEndDelimiter() {
        return "*/";
    }

    /*
     *  Override for other languages
     */
    public String getSingleLineDelimiter() {
        return "//";
    }

    /*
     *  Override for other languages
     */
    public String getEscapeString(String quoteDelimiter) {
        return "\\" + quoteDelimiter;
    }

    /*
     *
     */
    /*protected String addMatchingBrace(int offset) throws BadLocationException {
    StringBuffer whiteSpace = new StringBuffer();
    int line = rootElement.getElementIndex(offset);
    int i = rootElement.getElement(line).getStartOffset();
    
    while (true) {
    String temp = doc.getText(i, 1);
    
    if (temp.equals(" ") || temp.equals("\t")) {
    whiteSpace.append(temp);
    i++;
    } else {
    break;
    }
    }
    
    return "{\n" + whiteSpace.toString() + "\t\n" + whiteSpace.toString() + "}";
    }*/
}
