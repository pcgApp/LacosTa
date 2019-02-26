package com.rakantao.pcg.lacostazamboanga.PCGAdmin.Datas;

public class DataVesselSched {

    private String VesselType;
    private String VesselName;
    private String Origin;
    private String DepartureTime;
    private String Destination;
    private String ArrivalTime;
    private String ScheduleDay;
    private String ActualDepartedTime;
    private String Key;
    private String VesselStatus;
    private String OriginStation;
    private String DestinationStation;
    private String PassengerCapacity;
    private String NumberOfCrew;
    private String DistressStatus;
    private String DestinationSubStation;
    private String OriginSubStation;
    private String ToAppear;
    private String Remarks;

    public DataVesselSched(){

    }

    public DataVesselSched(String VesselType, String VesselName, String ScheduleDay, String Origin, String DepartureTime,
                           String Destination, String ArrivalTime, String ActualDepartedTime, String Key, String VesselStatus,
                           String OriginStation, String DestinationStation, String PassengerCapacity, String NumberOfCrew,
                           String DistressStatus, String OriginSubStation, String DestinationSubStation, String ToAppear, String Remarks){

        this.VesselType = VesselType;
        this.VesselName = VesselName;
        this.ScheduleDay = ScheduleDay;
        this.Origin = Origin;
        this.DepartureTime = DepartureTime;
        this.Destination = Destination;
        this.ArrivalTime = ArrivalTime;
        this.ActualDepartedTime = ActualDepartedTime;
        this.Key = Key;
        this.VesselStatus = VesselStatus;
        this.OriginStation = OriginStation;
        this.DestinationStation = DestinationStation;
        this.PassengerCapacity = PassengerCapacity;
        this.NumberOfCrew = NumberOfCrew;
        this.DistressStatus = DistressStatus;
        this.DestinationSubStation = DestinationSubStation;
        this.OriginSubStation = OriginSubStation;
        this.ToAppear = ToAppear;
        this.Remarks = Remarks;


    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getToAppear() {
        return ToAppear;
    }

    public void setToAppear(String toAppear) {
        ToAppear = toAppear;
    }

    public String getDestinationSubStation() {
        return DestinationSubStation;
    }

    public void setDestinationSubStation(String destinationSubStation) {
        DestinationSubStation = destinationSubStation;
    }

    public String getOriginSubStation() {
        return OriginSubStation;
    }

    public void setOriginSubStation(String originSubStation) {
        OriginSubStation = originSubStation;
    }

    public String getDistressStatus() {
        return DistressStatus;
    }

    public void setDistressStatus(String distressStatus) {
        DistressStatus = distressStatus;
    }

    public String getPassengerCapacity() {
        return PassengerCapacity;
    }

    public void setPassengerCapacity(String passengerCapacity) {
        PassengerCapacity = passengerCapacity;
    }

    public String getNumberOfCrew() {
        return NumberOfCrew;
    }

    public void setNumberOfCrew(String numberOfCrew) {
        NumberOfCrew = numberOfCrew;
    }

    public String getOriginStation() {
        return OriginStation;
    }

    public void setOriginStation(String originStation) {
        OriginStation = originStation;
    }

    public String getDestinationStation() {
        return DestinationStation;
    }

    public void setDestinationStation(String destinationStation) {
        DestinationStation = destinationStation;
    }

    public String getVesselStatus() {
        return VesselStatus;
    }

    public void setVesselStatus(String vesselStatus) {
        VesselStatus = vesselStatus;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getVesselType() {
        return VesselType;
    }

    public String getActualDepartedTime() {
        return ActualDepartedTime;
    }

    public void setActualDepartedTime(String actualDepartedTime) {
        ActualDepartedTime = actualDepartedTime;
    }

    public void setVesselType(String vesselType) {
        VesselType = vesselType;
    }

    public String getVesselName() {
        return VesselName;
    }

    public void setVesselName(String vesselName) {
        VesselName = vesselName;
    }

    public String getOrigin() {
        return Origin;
    }

    public void setOrigin(String origin) {
        Origin = origin;
    }

    public String getDepartureTime() {
        return DepartureTime;
    }

    public void setDepartureTime(String departureTime) {
        DepartureTime = departureTime;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getArrivalTime() {
        return ArrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        ArrivalTime = arrivalTime;
    }

    public String getScheduleDay() {
        return ScheduleDay;
    }

    public void setScheduleDay(String scheduleDay) {
        ScheduleDay = scheduleDay;
    }
}
