import React, { useEffect, useState } from "react";
import InputField from "../components/InputField";
import SelectField from "../components/SelectField";
import { useAuth } from "../services/AuthContext";
import apiClient from "../services/AxiosConfig";
import NavBar from "../components/Navbar";
import { BACKEND_URL } from "../config";
import { useNavigate, useSearchParams } from "react-router-dom";
import DateTimePicker from "../components/DateTimePicker";

const CreateMeeting = () => {
  const meetingTypes = ["ONLINE", "OFFLINE", "HYBRID"];
  const navigate = useNavigate();
  const [meetingDetails, setMeetingDetails] = useState({});
  const { user } = useAuth();
  const [validationError, setValidationError] = useState("");
  const [searchParams] = useSearchParams();
  const courseId = searchParams.get("courseId");
  const [addresses, setAddresses] = useState(null);

  const getAddresses = async () => {
    try {
      const res = await fetch(`${BACKEND_URL}/user/get-addresses`);
      if (res.status === 200) {
        const data = await res.json();
        setAddresses(data);
      }
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    getAddresses();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setMeetingDetails((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!courseId) return;
    setValidationError("");

    const startDate = new Date(meetingDetails.startDateTime);
    const endDate = new Date(meetingDetails.endDateTime);

    if (startDate >= endDate) {
      setValidationError("End date must be after start date.");
      return;
    }

    let requestBody;

    if (meetingDetails.meetingType === "ONLINE") {
      requestBody = {
        tutorId: user?.id,
        courseId: Number(courseId),
        startTime: meetingDetails.startDateTime + ":00",
        endTime: meetingDetails.endDateTime + ":00",
        meetingType: meetingDetails.meetingType,
      };
    } else if (
      meetingDetails.meetingType === "OFFLINE" ||
      meetingDetails.meetingType === "HYBRID"
    ) {
      requestBody = {
        tutorId: user?.id,
        courseId: Number(courseId),
        startTime: meetingDetails.startDateTime + ":00",
        endTime: meetingDetails.endDateTime + ":00",
        meetingType: meetingDetails.meetingType,
        roomNum: meetingDetails.roomNum,
        addressId: meetingDetails.addressId,
      };
    }
    try {
      console.log(requestBody);
      await apiClient.post("/tutor/create-meeting", requestBody);
      navigate("/course?id=" + courseId);
    } catch (error) {
      if (error.response?.status === 409) {
        alert("A course with similar details already exists.");
      } else {
        console.error("Error submitting data:", error);
        alert("An error occurred. Please try again.");
      }
    }
  };

  return (
    <div className="flex flex-col items-center w-full bg-white overflow-hidden font-merriweather_sans">
      <NavBar currentPage="/tutor-centre" />
      <div className="mb-6 mt-[120px]">
        <h1 className="text-3xl font-semibold text-gray-800 mb-2">
          Create a New Meetings
        </h1>
      </div>
      <form onSubmit={handleSubmit} className="w-full max-w-4xl">
        {validationError && (
          <div className="text-red-500 text-sm">{validationError}</div>
        )}

        <SelectField
          label="Meetings Type *"
          name="meetingType"
          value={meetingDetails.meetingType || ""}
          onChange={handleChange}
          options={meetingTypes}
          required={true}
        />
        {(meetingDetails.meetingType === "ONLINE" ||
          meetingDetails.meetingType === "HYBRID") && (
          <div className="mt-2 font-semibold italic text-gray-500">
            ** Contact the tutor for the meeting link **
          </div>
        )}
        {(meetingDetails.meetingType === "OFFLINE" ||
          meetingDetails.meetingType === "HYBRID") && (
          <>
            <div className="mt-4" />

            <SelectField
              label="Address *"
              name="addressId"
              value={meetingDetails.addressId}
              onChange={handleChange}
              options={addresses}
              required={true}
            />

            <InputField
              label="Room Number *"
              placeholder="A108"
              name="roomNum"
              value={meetingDetails.roomNum}
              onChange={handleChange}
              required={true}
            />
          </>
        )}

        <DateTimePicker
          label="Start Date/Time *"
          onChange={handleChange}
          required={true}
          name="startDateTime"
          value={meetingDetails.startDateTime}
        />

        <DateTimePicker
          label="End Date/Time *"
          onChange={handleChange}
          required={true}
          name="endDateTime"
          value={meetingDetails.endDateTime}
        />

        <button
          type="submit"
          className="bg-blue-800 max-h-12 mt-4 w-full max-w-xs rounded-full text-white py-2 px-4"
        >
          Submit
        </button>
      </form>
    </div>
  );
};

export default CreateMeeting;
