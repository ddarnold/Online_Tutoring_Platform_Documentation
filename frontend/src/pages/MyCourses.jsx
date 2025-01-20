import React, { useCallback, useEffect, useState } from "react";
import NavBar from "../components/Navbar";
import CourseSearchResultItem from "../components/CourseSearchResultItem";
import { useNavigate } from "react-router-dom";
import { BACKEND_URL } from "../config";
import apiClient from "../services/AxiosConfig";
import { useAuth } from "../services/AuthContext";

function MyCourses() {
  const navigate = useNavigate();
  const [courses, setCourses] = useState(null);
  const { user } = useAuth();

  const fetchStudentCourses = useCallback(async () => {
    try {
      const res = await apiClient.get("/student/enrolled-courses");
      if (res.status === 200) {
        setCourses(res.data);
      }
    } catch (error) {
      console.error("Error fetching student courses:", error);
    }
  }, [user]);

  useEffect(() => {
    if (user) {
      fetchStudentCourses();
    }
  }, [user, fetchStudentCourses]);

  return (
    <div className="flex flex-col items-center w-full bg-white overflow-hidden font-merriweather_sans">
      <NavBar currentPage="/my-courses" />
      <div className="w-full max-w-6xl mt-[120px] mb-10">
        <div className="flex items-center justify-between">
          <div className="flex flex-col">
            <div className="inline-flex items-center w-full">
              <div className="text-xl">Enrolled Courses</div>
            </div>
          </div>
        </div>
        {courses &&
          courses.map((result, index) => {
            return <CourseSearchResultItem course={result} key={index} />;
          })}
      </div>
    </div>
  );
}

export default MyCourses;
