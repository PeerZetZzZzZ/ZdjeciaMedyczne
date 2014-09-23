package model.regex;

import jregex.Pattern;
import model.ResourceBundleMaster;
import model.exception.RegexException;

/**
 * This class is used to verify given strings, such as username, password etc to
 * avoid SQL Injection.
 *
 * @author PeerZet
 */
public class RegexPatternChecker {

    private final String usernamePattern = "[a-z0-9_]{1,20}";//only a-z and 0-9 and _ only 20 signs
    private final String passwordPattern = "[\\w][^ '\"]{3,15}";// \\ because of java \w illegal character, 3-15 signs, no ' and " and space
    private final String singleWordPattern = "[a-zA-Z]{1,40}";// only a-Z and 1-40 signs
    private final String singleNumberPattern = "[\\d]{1,10}";// only integer max 10 signs
    private final String singleDescriptionPattern = "[\\w][^'\"]{0,200}";//description has 200 signs max
    private final String singleDiagnosisPattern = "[\\w][^'\"]{0,500}";//description has 200 signs max
    private final Pattern usernameRegex;
    private final Pattern passwordRegex;
    private final Pattern singleWordRegex;//used for name, surname,specialization and sex
    private final Pattern singleNumberRegex; //used for number, example: age
    private final Pattern singleDescriptionRegex;
    private final Pattern singleDiagnosisRegex;

    public RegexPatternChecker() {
        usernameRegex = new Pattern(usernamePattern);
        passwordRegex = new Pattern(passwordPattern);
        singleWordRegex = new Pattern(singleWordPattern);
        singleNumberRegex = new Pattern(singleNumberPattern);
        singleDescriptionRegex = new Pattern(singleDescriptionPattern);
        singleDiagnosisRegex = new Pattern(singleDiagnosisPattern);
    }

    public void verifyUser(String username) throws RegexException {
        if (username == null && username.equals("")) {
            throw new RegexException(ResourceBundleMaster.TRANSLATOR.getTranslation("nullValue"));
        }
        if (!usernameRegex.matcher(username).matches()) {//if not matches throw exception
            throw new RegexException(ResourceBundleMaster.TRANSLATOR.getTranslation("illegalUsername"));
        }

    }

    public void verifyPassword(String password) throws RegexException {
        if (password == null && password.equals("")) {
            throw new RegexException(ResourceBundleMaster.TRANSLATOR.getTranslation("nullValue"));
        }
        if (!passwordRegex.matcher(password).matches()) {//if not matches throw exception
            throw new RegexException(ResourceBundleMaster.TRANSLATOR.getTranslation("illegalPassword"));
        }

    }

    public void verifySingleWord(String singleWord) throws RegexException {
        if (singleWord == null && singleWord.equals("")) {
            throw new RegexException(ResourceBundleMaster.TRANSLATOR.getTranslation("nullValue"));
        }
        if (!singleWordRegex.matcher(singleWord).matches()) {//if not matches throw exception
            throw new RegexException(ResourceBundleMaster.TRANSLATOR.getTranslation("illegalSingleWord"));
        }

    }

    public void verifySingleNumber(String singleNumber) throws RegexException {
        if (singleNumber == null && singleNumber.equals("")) {
            throw new RegexException(ResourceBundleMaster.TRANSLATOR.getTranslation("nullValue"));
        }
        if (!singleNumberRegex.matcher(singleNumber).matches()) {//if not matches throw exception
            throw new RegexException(ResourceBundleMaster.TRANSLATOR.getTranslation("illegalSingleNumber"));
        }

    }

    public void verifySingleDescription(String description) throws RegexException {
        if (description == null && description.equals("")) {
            throw new RegexException(ResourceBundleMaster.TRANSLATOR.getTranslation("nullValue"));
        }
        if (!singleDescriptionRegex.matcher(description).matches()) {//if not matches throw exception
            throw new RegexException(ResourceBundleMaster.TRANSLATOR.getTranslation("illegalSingleDescription"));
        }
    }

    public void verifySingleDiagnosis(String diagnosis) throws RegexException {
        if (diagnosis == null && diagnosis.equals("")) {
            throw new RegexException(ResourceBundleMaster.TRANSLATOR.getTranslation("nullValue"));
        }
        if (!singleDescriptionRegex.matcher(diagnosis).matches()) {//if not matches throw exception
            throw new RegexException(ResourceBundleMaster.TRANSLATOR.getTranslation("illegalSingleDiagnosis"));
        }
    }
}
