package enums;

public enum Role {
    EMPLOYEE("employee"),
    MANAGER("manager"),
    ADMIN("admin");

    private final String role;
    Role (String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public static Role fromString(String role) {
        for (Role r : Role.values()) {
            if(r.getRole().equalsIgnoreCase(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No matching role for " + role);
    }
}
