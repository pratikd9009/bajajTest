package org.example;


public class PRNDestinationApp {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar PRNDestinationApp.jar 240340120137 ");
            return;
        }

        String prnNumber = args[0].toLowerCase().replaceAll("\\s", ""); // Convert to lowercase and remove spaces
        String jsonFilePath = args[1];

        try {
            // Parse the JSON file and get the value of "destination"
            String destinationValue = getDestinationValue(jsonFilePath);

            // Generate a random 8-character alphanumeric string
            String randomString = generateRandomString(8);

            // Concatenate the values and compute the MD5 hash
            String concatenatedString = prnNumber + destinationValue + randomString;
            String md5Hash = computeMD5Hash(concatenatedString);

            // Output the result
            System.out.println(md5Hash + ";" + randomString);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String getDestinationValue(String jsonFilePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(Paths.get(jsonFilePath).toFile());
        return findDestination(rootNode);
    }

    private static String findDestination(JsonNode node) {
        Iterator<String> fieldNames = node.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode childNode = node.get(fieldName);

            if (fieldName.equals("destination")) {
                return childNode.asText();
            } else if (childNode.isObject() || childNode.isArray()) {
                String result = findDestination(childNode);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            stringBuilder.append(characters.charAt(random.nextInt(characters.length())));
        }

        return stringBuilder.toString();
    }

    private static String computeMD5Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());
        byte[] digest = md.digest();

        StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}










