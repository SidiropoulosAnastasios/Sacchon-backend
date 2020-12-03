package gr.codehub.team2.Sacchon.representation;

import gr.codehub.team2.Sacchon.model.Doctor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DoctorRepresentation {
    private String name;
    private String doctorPermissionCode;
    private boolean active;
    private Long id;
    /**
     * The URL of this resource.
     */
    private String uri;

    /** This method is used in order to match Doctor JSON Representation to Doctor data.
     *
     * @param doctorRepresentation
     * @return Doctor Data.
     */
    static public Doctor getDoctor(DoctorRepresentation doctorRepresentation) {
        Doctor doctor = new Doctor();
        doctor.setName(doctorRepresentation.getName());
        doctor.setDoctorPermissionCode(doctorRepresentation.getDoctorPermissionCode());
        return doctor;
    }

    /** This method is used in order to represent Doctor in JSON format.
     *
     * @param doctor
     * @return JSON Representation of Doctor data.
     */
    static public DoctorRepresentation getDoctorRepresentation(Doctor doctor) {
        DoctorRepresentation doctorRepresentation = new DoctorRepresentation();
        doctorRepresentation.setId(doctor.getId());
        doctorRepresentation.setName(doctor.getName());
        doctorRepresentation.setDoctorPermissionCode(doctor.getDoctorPermissionCode());
        doctorRepresentation.setUri("http://localhost:9000/sacchon/doctors/" + doctor.getId());
        return doctorRepresentation;
    }
}