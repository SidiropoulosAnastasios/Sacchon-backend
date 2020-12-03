package gr.codehub.team2.Sacchon.router;

import gr.codehub.team2.Sacchon.resource.DoctorAdvice.impl.*;
import gr.codehub.team2.Sacchon.resource.MediDataRepo.impl.*;
import gr.codehub.team2.Sacchon.resource.PingServerResource;
import gr.codehub.team2.Sacchon.resource.Reporter.impl.*;
import gr.codehub.team2.Sacchon.resource.login.LoginResourceImpl;
import gr.codehub.team2.Sacchon.resource.register.impl.RegisterDoctorResourceImpl;
import gr.codehub.team2.Sacchon.resource.register.impl.RegisterResourceImpl;
import org.restlet.Application;
import org.restlet.routing.Router;

public class CustomRouter {

    private Application application;

    public CustomRouter(Application application) {
        this.application = application;

    }

    public Router createApiRouter() {

        Router router = new Router(application.getContext());

        router.attach("/patients/{patient_id}", PatientResourceImpl.class);
        router.attach("/patients/{patient_id}/", PatientResourceImpl.class);
        router.attach("/patients/{patient_id}/measurements", MeasurementListResourceImpl.class);
        router.attach("/patients/{patient_id}/measurements/", MeasurementListResourceImpl.class);
        router.attach("/patients/{patient_id}/measurements/{measurement_id}", MeasurementResourceImpl.class);
        router.attach("/patients/{patient_id}/measurements/{measurement_id}/", MeasurementResourceImpl.class);
        router.attach("/patients/{patient_id}/measurements/average/{start_date}/{end_date}", MeasurementAverageResourceImpl.class);
        router.attach("/patients/{patient_id}/measurements/average/{start_date}/{end_date}/", MeasurementAverageResourceImpl.class);
        router.attach("/patients/{patient_id}/consultations", ConsultationListResourceImpl.class);
        router.attach("/patients/{patient_id}/consultations/", ConsultationListResourceImpl.class);

        //consultation can be seen from patient and doctor
        router.attach("/patients/{patient_id}/consultations/{consultation_id}", ConsultationResourceImpl.class);
        router.attach("/patients/{patient_id}/consultations/{consultation_id}/", ConsultationResourceImpl.class);

        router.attach("/doctors/{doctor_id}", DoctorResourceImpl.class);
        router.attach("/doctors/{doctor_id}/", DoctorResourceImpl.class);
        router.attach("/doctors/{doctor_id}/my-patients", DoctorPatientListResourceImpl.class);
        router.attach("/doctors/{doctor_id}/my-patients/", DoctorPatientListResourceImpl.class);
        router.attach("/doctors/{doctor_id}/available-patients", DoctorAvailablePatientListResourceImpl.class);
        router.attach("/doctors/{doctor_id}/available-patients/", DoctorAvailablePatientListResourceImpl.class);
        router.attach("/doctors/{doctor_id}/patients-waiting-consultation", PatientsWaitingConsultationListResourceImpl.class);
        router.attach("/doctors/{doctor_id}/patients-waiting-consultation/", PatientsWaitingConsultationListResourceImpl.class);
        router.attach("/doctors/{doctor_id}/patients/{patient_id}/measurements", DoctorPatientMeasurementListResourceImpl.class);
        router.attach("/doctors/{doctor_id}/patients/{patient_id}/measurements/", DoctorPatientMeasurementListResourceImpl.class);
        router.attach("/doctors/{doctor_id}/patients/{patient_id}/consultations", ConsultationListResourceImpl.class);
        router.attach("/doctors/{doctor_id}/patients/{patient_id}/consultations/", ConsultationListResourceImpl.class);

        router.attach("/reporter/patients", PatientListResourceImpl.class);
        router.attach("/reporter/patients/", PatientListResourceImpl.class);
        router.attach("/reporter/patients/{patient_id}/measurements/{start_date}/{end_date}", MeasurementsOfPatientOverTimeRangeResourceImpl.class);
        router.attach("/reporter/patients/{patient_id}/measurements/{start_date}/{end_date}/", MeasurementsOfPatientOverTimeRangeResourceImpl.class);
        router.attach("/reporter/patients-waiting-consultation", PatientListWaitingConsultationAndTimeElapsedResourceImpl.class);
        router.attach("/reporter/patients-waiting-consultation/", PatientListWaitingConsultationAndTimeElapsedResourceImpl.class);
        router.attach("/reporter/patients-with-no-activity/{start_date}/{end_date}", PatientListNoActivityResourceImpl.class);
        router.attach("/reporter/patients-with-no-activity/{start_date}/{end_date}/", PatientListNoActivityResourceImpl.class);
        router.attach("/reporter/doctors", DoctorListResourceImpl.class);
        router.attach("/reporter/doctors/", DoctorListResourceImpl.class);
        router.attach("/reporter/doctors/{doctor_id}/consultations/{start_date}/{end_date}", ConsultationsOfDoctorOverTimeRangeResourceImpl.class);
        router.attach("/reporter/doctors/{doctor_id}/consultations/{start_date}/{end_date}/", ConsultationsOfDoctorOverTimeRangeResourceImpl.class);
        router.attach("/reporter/doctors-with-no-activity/{start_date}/{end_date}", DoctorsListNoActivityResourceImpl.class);
        router.attach("/reporter/doctors-with-no-activity/{start_date}/{end_date}/", DoctorsListNoActivityResourceImpl.class);

        return router;
    }

    public Router publicResources() {
        Router router = new Router();
        router.attach("/ping", PingServerResource.class);
        router.attach("/login", LoginResourceImpl.class);
        router.attach("/login/", LoginResourceImpl.class);
        router.attach("/register", RegisterResourceImpl.class);
        router.attach("/register/", RegisterResourceImpl.class);
        router.attach("/register-doctor", RegisterDoctorResourceImpl.class);
        router.attach("/register-doctor/", RegisterDoctorResourceImpl.class);
        return router;
    }
}
