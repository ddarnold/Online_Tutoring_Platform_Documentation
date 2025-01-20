import React, { useEffect, useState } from "react";
import InputField from "../components/InputField";
import TextareaField from "../components/TextareaField";
import SelectField from "../components/SelectField";
import { useAuth } from "../services/AuthContext";
import apiClient from "../services/AxiosConfig";
import NavBar from "../components/Navbar";
import { BACKEND_URL } from "../config";
import { useNavigate } from "react-router-dom";

const CreateCourse = () => {
  const navigate = useNavigate();
  const [courseDetails, setCourseDetails] = useState({});
  const { user } = useAuth();
  const [validationError, setValidationError] = useState("");
  const [shortDescriptionLength, setShortDescriptionLength] = useState(0);
  const [longDescriptionLength, setLongDescriptionLength] = useState(0);

  const [categories, setCategories] = useState([]);

  const fetchCategories = async () => {
    const response = await fetch(`${BACKEND_URL}/search/categories`);
    const data = await response.json();
    setCategories(data);
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  const MAX_SHORT_DESC = 150;
  const MAX_LONG_DESC = 2000;

  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name === "shortDescription") {
      if (value.length <= MAX_SHORT_DESC) {
        setShortDescriptionLength(value.length);
        setCourseDetails({ ...courseDetails, [name]: value });
      }
    } else if (name === "longDescription") {
      if (value.length <= MAX_LONG_DESC) {
        setLongDescriptionLength(value.length);
        setCourseDetails({ ...courseDetails, [name]: value });
      }
    } else {
      setCourseDetails({ ...courseDetails, [name]: value });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setValidationError("");

    const startDate = new Date(courseDetails.startDate);
    const endDate = new Date(courseDetails.endDate);

    if (startDate >= endDate) {
      setValidationError("End date must be after start date.");
      return;
    }

    const requestBody = {
      courseName: courseDetails.courseName,
      tutorId: user?.id,
      descriptionShort: courseDetails.shortDescription,
      descriptionLong: courseDetails.longDescription,
      startDate: courseDetails.startDate,
      endDate: courseDetails.endDate,
      averageRating: 0,
      courseCategories: [
        {
          categoryId:
            categories.find(
              (category) => category.name === courseDetails.category,
            )?.id || 0,
          categoryName: courseDetails.category,
        },
      ],
    };

    try {
      await apiClient.post("/tutor/course/create", requestBody);

      navigate("/tutor-centre");
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
          Create a New Course
        </h1>
      </div>
      <form onSubmit={handleSubmit} className="w-full max-w-4xl">
        {validationError && (
          <div className="text-red-500 text-sm">{validationError}</div>
        )}

        <InputField
          label="Course Name *"
          placeholder="Software Engineering"
          name="courseName"
          value={courseDetails.courseName || ""}
          onChange={handleChange}
          required={true}
        />

        <TextareaField
          label="Short Description *"
          placeholder="Enter a short description"
          name="shortDescription"
          value={courseDetails.shortDescription || ""}
          onChange={handleChange}
          rows={2}
          maxLength={MAX_SHORT_DESC}
          hint={`Max ${MAX_SHORT_DESC} characters.`}
          required={true}
        />
        <p
          className={`text-xs mb-4 ${
            shortDescriptionLength === MAX_SHORT_DESC ? "text-red-500" : ""
          }`}
        >
          {shortDescriptionLength}/{MAX_SHORT_DESC}
        </p>

        <TextareaField
          label="Long Description *"
          placeholder="Enter a detailed description"
          name="longDescription"
          rows={6}
          value={courseDetails.longDescription || ""}
          onChange={handleChange}
          maxLength={MAX_LONG_DESC}
          hint={`Max ${MAX_LONG_DESC} characters.`}
          required={true}
        />
        <p className="text-xs mb-4">
          {longDescriptionLength}/{MAX_LONG_DESC}
        </p>

        <SelectField
          label="Category *"
          name="category"
          value={courseDetails.category || ""}
          onChange={handleChange}
          options={categories}
          required={true}
        />

        <InputField
          label="Start Date *"
          type="date"
          name="startDate"
          value={courseDetails.startDate || ""}
          onChange={handleChange}
          required={true}
        />

        <InputField
          label="End Date *"
          type="date"
          name="endDate"
          value={courseDetails.endDate || ""}
          onChange={handleChange}
          required={true}
        />

        <button
          type="submit"
          className="bg-blue-800 max-h-12 w-full max-w-xs rounded-full text-white py-2 px-4"
        >
          Submit
        </button>
      </form>
    </div>
  );
};

export default CreateCourse;
