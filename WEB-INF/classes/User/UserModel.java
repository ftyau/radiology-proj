package User;
public class UserModel{
        private String firstName;
        private String lastName;
        private String address;
        private String email;
        private String phone;

        //Setters and Getters for first Name
        public void setFirstName(String firstName){
                this.firstName = firstName;
        }

        public String getFirstName(){
                return firstName;
        }

        //Setters and getters for last name
        public void setLastName(String lastName){
                this.lastName = lastName;
        }

        public String getLastName(){
                return lastName;
        }

        //Setters and getters for address
        public void setAddress(String address){
                this.address = address;
        }

        public String getAddress(){
                return address;
        }

        //Setters and Getters for email
        public void setEmail(String email){
                this.email = email;
        }

        public String getEmail(){
                return email;
        }

        //Setters and Getters for phone number
        public void setPhone(String phone){
                this.phone = phone;
        }

        public String getPhone(){
                return phone;
        }

}