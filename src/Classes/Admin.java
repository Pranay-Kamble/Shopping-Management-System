package Classes;

public final class Admin extends Person{
//    private static int ID = 1;
    private String adminId;
    private String password;
    private String name;

    public Admin(Admin copyAdmin) {
        this.adminId = copyAdmin.adminId;
        this.password = copyAdmin.password;
        this.name = copyAdmin.name;
    }

    public Admin() { }

    public Admin(String adminId, String name, String password) {
        this.adminId = adminId;
        this.password = password;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getAdminId() {
        return this.adminId;
    }

    public String getPassword(){
        return this.password;
    }

    public String toString() {
        return "Admin ID: " + this.adminId + "\nAdmin Name: " + this.name;
    }
}