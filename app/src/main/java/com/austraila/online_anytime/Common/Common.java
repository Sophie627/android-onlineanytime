package com.austraila.online_anytime.Common;

public class Common {
    private String ApiKey = "?api_key=54d0a2c6b96b514cb47c3645714f7ce8";
    private String registerUrl = "register";
    private String TestloginUrl = "http://192.168.107.100/api/login";
    private String TestbaseUrl = "http://192.168.107.100/api/";
    private String mainItemUrl = "forms";
    private String formelementUrl = "http://192.168.107.100/api/form_elements/";
    private String elemnetOptionUrl = "http://192.168.107.100/api/form_elements_options";
    private String forgetUrl = "http://192.168.107.100/api/forget";
    private String codeUrl = "http://192.168.107.100/api/forget/valid";
    private String setpassUrl = "http://192.168.107.100/api/forget/setpassword";
    private String saveUrl = "http://192.168.107.100/api/form/save";

//    private String TestloginUrl = "http://online-anytime.com.au/olat/newapi/login";
//    private String TestbaseUrl = "http://online-anytime.com.au/olat/newapi/";
//    private String mainItemUrl = "forms";
//    private String formelementUrl = "http://online-anytime.com.au/olat/newapi/form_elements/";
//    private String elemnetOptionUrl = "http://online-anytime.com.au/olat/newapi/form_elements_options";
//    private String forgetUrl = "http://online-anytime.com.au/olat/newapi/forget";
//    private String codeUrl = "http://online-anytime.com.au/olat/newapi/forget/valid";
//    private String setpassUrl = "http://online-anytime.com.au/olat/newapi/forget/setpassword";
//    private String saveUrl = "http://online-anytime.com.au/olat/newapi/form/save";

    private static Common instance = new Common();

    public static Common getInstance()
    {
        return instance;
    }

    public String getBaseURL() {
        return TestloginUrl;
    }
    public String getApiKey() {
        return ApiKey;
    }

    public String getFormelementUrl(){
        return formelementUrl;
    }

    public String getMainItemUrl() { return TestbaseUrl + mainItemUrl;}

    public String getElemnetOptionUrl() {
        return elemnetOptionUrl;
    }

    public String getRegisterUrl(){
        return TestbaseUrl + registerUrl;
    }

    public String getForgetUrl() {return forgetUrl;}

    public String getCodeUrl() {return  codeUrl;}

    public String getSetpassUrl(){return setpassUrl;}

    public String getSaveUrl(){
        return saveUrl;
    }

    public String[] countryArray ={
            "United States"
            ,"United Kingdom"
            ,"Canada"
            ,"Australia"
            ,"Netherlands"
            ,"France"
            ,"Germany"
            ,"-------"
            ,"Afghanistan"
            ,"Albania"
            ,"Algeria"
            ,"Andorra"
            ,"Antigua and Barbuda"
            ,"Argentina"
            ,"Armenia"
            ,"Austria"
            ,"Azerbaijan"
            ,"Bahamas"
            ,"Bahrain"
            ,"Bangladesh"
            ,"Barbados"
            ,"Belarus"
            ,"Belgium"
            ,"Belize"
            ,"Benin"
            ,"Bermuda"
            ,"Bhutan"
            ,"Bolivia"
            ,"Bosnia and Herzegovina"
            ,"Botswana"
            ,"Brazil"
            ,"Brunei"
            ,"Bulgaria"
            ,"Burkina Faso"
            ,"Burundi"
            ,"Cambodia"
            ,"Cameroon"
            ,"Cape Verde"
            ,"Cayman Islands"
            ,"Central African Republic"
            ,"Chad"
            ,"Chile"
            ,"China"
            ,"Colombia"
            ,"Comoros"
            ,"Congo"
            ,"Costa Rica"
            ,"Côte d'Ivoire"
            ,"Croatia"
            ,"Cuba"
            ,"Cyprus"
            ,"Czech Republic"
            ,"Denmark"
            ,"Djibouti"
            ,"Dominica"
            ,"Dominican Republic"
            ,"East Timor"
            ,"Ecuador"
            ,"Egypt"
            ,"El Salvador"
            ,"Equatorial Guinea"
            ,"Eritrea"
            ,"Estonia"
            ,"Ethiopia"
            ,"Fiji"
            ,"Finland"
            ,"Gabon"
            ,"Gambia"
            ,"Georgia"
            ,"Ghana"
            ,"Gibraltar"
            ,"Greece"
            ,"Grenada"
            ,"Guatemala"
            ,"Guernsey"
            ,"Guinea"
            ,"Guinea-Bissau"
            ,"Guyana"
            ,"Haiti"
            ,"Honduras"
            ,"Hong Kong"
            ,"Hungary"
            ,"Iceland"
            ,"India"
            ,"Indonesia"
            ,"Iran"
            ,"Iraq"
            ,"Ireland"
            ,"Israel"
            ,"Italy"
            ,"Jamaica"
            ,"Japan"
            ,"Jordan"
            ,"Kazakhstan"
            ,"Kenya"
            ,"Kiribati"
            ,"North Korea"
            ,"South Korea"
            ,"Kuwait"
            ,"Kyrgyzstan"
            ,"Laos"
            ,"Latvia"
            ,"Lebanon"
            ,"Lesotho"
            ,"Liberia"
            ,"Libya"
            ,"Liechtenstein"
            ,"Lithuania"
            ,"Luxembourg"
            ,"Macedonia"
            ,"Madagascar"
            ,"Malawi"
            ,"Malaysia"
            ,"Maldives"
            ,"Mali"
            ,"Malta"
            ,"Marshall Islands"
            ,"Mauritania"
            ,"Mauritius"
            ,"Mexico"
            ,"Micronesia"
            ,"Moldova"
            ,"Monaco"
            ,"Mongolia"
            ,"Montenegro"
            ,"Morocco"
            ,"Mozambique"
            ,"Myanmar"
            ,"Namibia"
            ,"Nauru"
            ,"Nepal"
            ,"New Zealand"
            ,"Nicaragua"
            ,"Niger"
            ,"Nigeria"
            ,"Norway"
            ,"Oman"
            ,"Pakistan"
            ,"Palau"
            ,"Palestine"
            ,"Panama"
            ,"Papua New Guinea"
            ,"Paraguay"
            ,"Peru"
            ,"Philippines"
            ,"Poland"
            ,"Portugal"
            ,"Puerto Rico"
            ,"Qatar"
            ,"Romania"
            ,"Russia"
            ,"Rwanda"
            ,"Saint Kitts and Nevis"
            ,"Saint Lucia"
            ,"Saint Vincent and the Grenadines"
            ,"Samoa"
            ,"San Marino"
            ,"Sao Tome and Principe"
            ,"Saudi Arabia"
            ,"Senegal"
            ,"Serbia and Montenegro"
            ,"Seychelles"
            ,"Sierra Leone"
            ,"Singapore"
            ,"Slovakia"
            ,"Slovenia"
            ,"Solomon Islands"
            ,"Somalia"
            ,"South Africa"
            ,"Spain"
            ,"Sri Lanka"
            ,"Sudan"
            ,"Suriname"
            ,"Swaziland"
            ,"Sweden"
            ,"Switzerland"
            ,"Syria"
            ,"Taiwan"
            ,"Tajikistan"
            ,"Tanzania"
            ,"Thailand"
            ,"Togo"
            ,"Tonga"
            ,"Trinidad and Tobago"
            ,"Tunisia"
            ,"Turkey"
            ,"Turkmenistan"
            ,"Tuvalu"
            ,"Uganda"
            ,"Ukraine"
            ,"United Arab Emirates"
            ,"Uruguay"
            ,"Uzbekistan"
            ,"Vanuatu"
            ,"Vatican City"
            ,"Venezuela"
            ,"Vietnam"
            ,"Yemen"
            ,"Zambia"
            ,"Zimbabwe"
    };
}
