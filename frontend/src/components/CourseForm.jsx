import React, { useState, useEffect } from "react";
import InputField from "./InputField";
import TextareaField from "./TextareaField";
import SelectField from "./SelectField";
import FileUpload from "./FileUpload";
import { useAuth } from "../services/AuthContext"; //
import apiClient from "../services/AxiosConfig";

//TO verify functionality i used mock testing:)
const CourseForm = () => {
  const [formSchema, setFormSchema] = useState([])
  const [courseDetails, setCourseDetails] = useState({}); //dynamically updated
  const { user} = useAuth();
  const [conflictError, setConflictError] = useState(false);

  //fetch the form schema from the backend API
  useEffect(() => {
    async function fetchSchema() {
      try {
        const response = await fetch("http://localhost:8080/tutor/course/create");
        if (!response.ok) {
          throw new Error(`Failed to fetch schema. Status: ${response.status}`);
        }
        const schema = await response.json();
        setFormSchema(schema);
      } catch (error) {
        console.error("Error fetching form schema:", error);
        alert("Failed to fetch form schema. Please check your backend.");
      }
    }
    fetchSchema();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCourseDetails({ ...courseDetails, [name]: value });
  };

  const handleImageUpload = (file) => {
    setCourseDetails({ ...courseDetails, image: file });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setConflictError(false);

    const requestBody = {
      courseName: courseDetails.courseName,
      tutorId: user?.id,
      descriptionShort: courseDetails.shortDescription,
      descriptionLong: courseDetails.longDescription,
      startDate: courseDetails.startDate,
      endDate: courseDetails.endDate,
    };

    try {

      const { data } = await apiClient.post(
          "/tutor/course/create",
          requestBody
      );
      // If successful, you can handle success here
      alert("Course successfully created!");
      console.log("Response:", data);

      // Reset form fields
      setCourseDetails({
        courseName: "",
        shortDescription: "",
        longDescription: "",
        category: "",
        startDate: "",
        endDate: "",
        image: null,
      });
    } catch (error) {
      // Handle conflict or other errors
      if (error.response?.status === 409) {
        setConflictError(true);
        alert("A course with similar details already exists.");
      } else {
        console.error("Error submitting data:", error);
        alert("An error occurred. Please try again.");
      }
    }
  };

  return (
      <div className="flex flex-col justify-center items-center w-full h-full">
        <div className="mb-6">
          <h1 className="text-3xl font-semibold text-gray-800 mb-2">
            Tell us about your course
          </h1>
          <p className="text-gray-600">
            We'll use this information to customize your course. You can change it any time.
          </p>
        </div>
        <form onSubmit={handleSubmit} className="w-full max-w-4xl space-y-4">
          {formSchema.map((field, index) => {                         //different fields are dynamically generated based on schema fetched frombackend API
            switch (field.type) {
              case "text":
                return (
                    <InputField
                        key={index}
                        label={field.label}
                        placeholder={field.placeholder || ""}
                        name={field.name}
                        value={courseDetails[field.name] || ""}
                        onChange={handleChange}
                        required={field.required}
                    />
                );
              case "textarea":
                return (
                    <TextareaField
                        key={index}
                        label={field.label}
                        placeholder={field.placeholder || ""}
                        name={field.name}
                        value={courseDetails[field.name] || ""}
                        onChange={handleChange}
                        maxLength={field.maxLength}
                        hint={field.maxLength ? `Max ${field.maxLength} characters.` : ""}
                    />
                );
              case "select":
                return (
                    <SelectField
                        key={index}
                        label={field.label}
                        name={field.name}
                        value={courseDetails[field.name] || ""}
                        onChange={handleChange}
                        options={field.options || []}
                        required={field.required}
                    />
                );
              case "date":
                return (
                    <InputField
                        key={index}
                        label={field.label}
                        type="date"
                        name={field.name}
                        value={courseDetails[field.name] || ""}
                        onChange={handleChange}
                        required={field.required}
                    />
                );
              case "file":
                return (
                    <FileUpload
                        key={index}
                        label={field.label}
                        onFileSelect={handleImageUpload}
                    />
                );
              default:
                return null;
            }
          })}

          <button
              type="submit"
              className="bg-gray-200 rounded-full px-4 py-2 text-md font-merriweather_sans m-2 hover:bg-gray-300"
          >
            Submit
          </button>
        </form>
      </div>
  );
};

export default CourseForm;
