package ankit.dsa;

import java.util.regex.Pattern;

/**
 * Regex:
 * 0-9 : [0-9]
 * 10 - 99: [1-9][0-9]
 * 100-199: 1[0-9]{2}
 * 200-249: 2[0-4][0-9]
 * 250-255: 25[0-5]
 * */
public class ValidateIPAddress_LC468 {
    private static final Pattern IPv4_SEGMENT_PATTERN = Pattern.compile("[0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]");
    private static final Pattern IPv6_SEGMENT_PATTERN = Pattern.compile("^[0-9A-Fa-f]*$");
    public String validIPAddress(String queryIP) {
        IPValidator validator = new IPV4Validator();
        validator.setNext(new IPV6Validator())
                .setNext(new InvalidIPHandler());
        return validator.validate(queryIP);
    }


    abstract static class IPValidator{
        public abstract String validate(String ip);

        protected IPValidator next;

        public IPValidator setNext(IPValidator next){
            this.next = next;
            return next;
        }
    }

    static class IPV4Validator extends IPValidator{

        @Override
        public String validate(String ip) {
            String[] segments = ip.split("\\.");
            if(segments.length == 4 ){
                boolean isIPV4 = true;
                for(String segment : segments){
                    if(!IPv4_SEGMENT_PATTERN.matcher(segment).matches()){
                        isIPV4 = false;
                        break;
                    }
                }
                if(isIPV4){
                    return "IPv4";
                }
            }
            return next.validate(ip);
        }
    }

    static class IPV6Validator extends IPValidator{
        @Override
        public String validate(String ip) {
            String[] segments = ip.split(":");
            if(segments.length == 8 && !ip.startsWith(":") && !ip.endsWith(":")){
                boolean isIPV6 = true;
                for(String segment : segments){
                    if(segment.isEmpty() || segment.length() > 4 || !IPv6_SEGMENT_PATTERN.matcher(segment).matches()){
                        isIPV6 = false;
                        break;
                    }
                }
                if(isIPV6){
                    return "IPv6";
                }
            }
            return next.validate(ip);
        }
    }

    //Fallback Handler
    static class InvalidIPHandler extends IPValidator{

        @Override
        public String validate(String ip) {
            return "Neither";
        }
    }

    public static void main(String[] args) {
        ValidateIPAddress_LC468 obj = new ValidateIPAddress_LC468();
        //System.out.println(obj.validIPAddress("2001:0db8:85a3:0:0:8A2E:0370:7334:"));
        System.out.println(obj.validIPAddress("172.16.254.10"));
    }
}
