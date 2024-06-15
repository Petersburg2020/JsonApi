package nx.peter.api.json;

import nx.peter.api.json.io.JsonReader;

import static nx.peter.api.json.Json.fromJson;
import static nx.peter.api.json.Json.readModelInBackground;
import static nx.peter.api.json.JsonUtil.formatDate;

public class Main {
    static User user;

    public static void main(String[] args) {
        user = new IUser("Petersburgz", "uareimee@gmail.com", "Uareime@24");

        readModelInBackground(user, new OnReadJsonProgressListener() {
            @Override
            public void onStart(long startTimeInMillis) {
                println("Read started at: " + formatDate(startTimeInMillis));
                println();
            }

            @Override
            public void onCompleted(JsonReader json, long durationInMillis) {
                println();
                println(json.prettyPrint());

                user = fromJson(json.openRootObject(), IUser.class);

                if (user != null) {
                    user.setEmail("adesinaoluwabusola@gmail.com");


                    readModelInBackground(user, new OnReadJsonProgressListener() {
                        @Override
                        public void onStart(long startTimeInMillis) {
                            println("Read started at: " + formatDate(startTimeInMillis));
                            println();
                        }

                        @Override
                        public void onCompleted(JsonReader json, long durationInMillis) {
                            println();
                            println(json.prettyPrint());
                        }

                        @Override
                        public void onProgress(int percent, long durationInMillis) {
                            println("Progress: " + percent + "%");
                        }
                    });
                }
            }

            @Override
            public void onProgress(int percent, long durationInMillis) {
                println("Progress: " + percent + "%");
            }
        });


    }

    public interface User {
        String getName();
        String getEmail();
        String getPassword();
        void setName(String name);
        void setEmail(String email);
        void setPassword(String password);
    }

    public static class IUser implements User {
        protected String name, email, password;

        public IUser(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getEmail() {
            return email;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static void println() {
        System.out.println();
    }

    public static void println(Object what) {
        System.out.println(what);
    }
}