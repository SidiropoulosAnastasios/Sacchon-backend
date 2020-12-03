package gr.codehub.team2.Sacchon.security;

public enum CustomRole {
    ROLE_GUEST("guest", 0),
    ROLE_CHIEF_DOCTOR("chiefDoctor", 1),
    ROLE_PATIENT("patient", 2),
    ROLE_DOCTOR("doctor", 3);

    private final String roleName;
    private final int roleNumber;

    CustomRole(String roleName, int roleNumber) {
        this.roleName = roleName;
        this.roleNumber = roleNumber;
    }


    public String getRoleName() {
        return roleName;
    }

    public int getRoleNumber() {
        return roleNumber;
    }

    public static CustomRole getRoleValue(String roleParameter) {
        for (CustomRole role : CustomRole.values()) {
            if (roleParameter.equals(role.getRoleName()))
                return role;
        }
        return ROLE_GUEST;
    }

    public static CustomRole getRoleValueNumber(int roleParameter) {
        for (CustomRole role : CustomRole.values()) {
            if (roleParameter == role.getRoleNumber())
                return role;
        }
        return ROLE_GUEST;
    }
}
