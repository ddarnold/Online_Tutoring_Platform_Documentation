import { useNavigate } from "react-router-dom";
import React, { useEffect, useState } from "react";
import NavBar from "../components/Navbar";

const CustomDropdown = ({ selectedOption, setSelectedOption, options }) => {
  const [isOpen, setIsOpen] = useState(false);

  const toggleDropdown = () => {
    setIsOpen((prev) => !prev);
  };

  const selectOption = (option) => {
    setSelectedOption(option);
    setIsOpen(false);
  };

  return (
    <div className="text-left absolute top-1/2 right-1.5 -translate-y-1/2">
      <button
        onClick={toggleDropdown}
        className="flex items-center justify-between py-1.5 px-3 rounded-md font-merriweather_sans bg-gray-200"
      >
        <span>{selectedOption}</span>
        {!isOpen && (
          <span className="material-symbols-rounded">keyboard_arrow_down</span>
        )}
        {isOpen && (
          <span className="material-symbols-rounded">keyboard_arrow_up</span>
        )}
      </button>

      {isOpen && (
        <div
          className="absolute mt-2 w-40 bg-white rounded-md shadow-lg ring-1 ring-black ring-opacity-5"
          onMouseLeave={() => setIsOpen(false)}
        >
          <ul className="py-1">
            {options.map((option, index) => (
              <li
                key={index}
                onClick={() => selectOption(option)}
                className="px-4 py-2 cursor-pointer font-merriweather_sans text-gray-800 hover:bg-gray-100"
              >
                {option}
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};

const SearchSection = () => {
  const navigate = useNavigate();
  const [expanded, setExpanded] = useState(false);
  const options = ["Courses", "Tutors"];
  const [selectedOption, setSelectedOption] = useState(options[0]);
  const [value, setValue] = useState("");
  const [categories, setCategories] = useState([]);

  const fetchCategories = async () => {
    const response = await fetch("http://localhost:8080/search/categories");
    const data = await response.json();
    setCategories(data);
  };

  useEffect(() => {
    fetchCategories();
  }, [categories]);

  const handleKeyDown = (e) => {
    if (e.key === "Enter") {
      e.preventDefault();
      if (selectedOption === "Courses") {
        navigate("/search?courseName=" + value);
      } else if (selectedOption === "Tutors") {
        navigate("/search?tutorName=" + value);
      }
    }
  };

  const handleCategoryClick = (categoryName) => {
    navigate("/search?categoryName=" + categoryName);
  };
  return (
    <>
      <div className="mt-[150px] mb-[20px] block font-merriweather_sans text-dark text-center text-4xl">
        Find your perfect tutor
      </div>

      <div className="relative w-full max-w-xl mx-auto">
        <input
          type="text"
          placeholder="Search for"
          onKeyDown={handleKeyDown}
          onChange={(e) => setValue(e.target.value)}
          className="font-merriweather_sans w-full bg-transparent rounded-md border border-stroke border-black py-[10px] pr-3 pl-12 text-dark-6 outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-gray-2"
        />
        <span className="absolute top-1/2 left-4 -translate-y-1/2 material-symbols-rounded">
          search
        </span>
        <CustomDropdown
          selectedOption={selectedOption}
          options={options}
          setSelectedOption={setSelectedOption}
        />
      </div>

      <div className="mt-[30px] flex flex-wrap justify-center items-start w-full max-w-2xl mx-auto bg-white">
        {categories &&
          categories
            .map((label) => (
              <button
                key={label.categoryId}
                className="bg-gray-200 rounded-full px-4 py-2 text-md font-merriweather_sans m-2 hover:bg-gray-300"
                onClick={() => handleCategoryClick(label.categoryName)}
              >
                {label.categoryName}
              </button>
            ))
            .splice(0, 4)}

        {expanded &&
          categories
            .map((label) => (
              <button
                key={label.categoryId}
                className="bg-gray-200 rounded-full px-4 py-2 text-md font-merriweather_sans m-2 hover:bg-gray-300"
                onClick={() => handleCategoryClick(label.categoryName)}
              >
                {label.categoryName}
              </button>
            ))
            .splice(4)}

        <button
          className={`${
            expanded ? "bg-red-800" : "bg-black"
          } text-white rounded-full px-4 py-2 text-md font-merriweather_sans m-2`}
          onClick={() => setExpanded(!expanded)}
        >
          {expanded ? "See Less" : "See All"}
        </button>
      </div>
    </>
  );
};

const StatsSection = (props) => {
  return (
    <>
      <div className="mt-[75px] mb-[20px] flex w-full bg-blue-900 py-5">
        <div className="flex flex-col justify-center items-center w-1/3">
          <span className="text-white text-2xl font-merriweather_sans">
            {props.students}
          </span>
          <span className="text-white text-xl font-merriweather_sans">
            students
          </span>
        </div>
        <div className="flex flex-col justify-center items-center w-1/3">
          <span className="text-white text-2xl font-merriweather_sans">
            {props.courses}
          </span>
          <span className="text-white text-xl font-merriweather_sans">
            courses
          </span>
        </div>
        <div className="flex flex-col justify-center items-center w-1/3">
          <span className="text-white text-2xl font-merriweather_sans">
            {props.tutors}
          </span>
          <span className="text-white text-xl font-merriweather_sans">
            tutors
          </span>
        </div>
      </div>
    </>
  );
};

const FeaturesSection = () => {
  return (
    <>
      <div className="mt-[50px] text-3xl font-merriweather_sans text-dark-6 text-center mb-[20px]">
        Why should you choose us?
      </div>
      <div className="mt-[20px] mb-[20px] flex w-full max-w-7xl bg-white py-5 items-center justify-center gap-x-20">
        <div className="flex flex-col self-start w-1/4 bg-gray-300 p-8 rounded-3xl">
          <span className="mb-[10px] material-symbols-rounded text-5xl">
            school
          </span>
          <span className="text-black text-2xl font-merriweather_sans">
            Trusted
            <br />
            Tutors
          </span>
          <span className="text-gray-700 text-md font-merriweather_sans">
            Our tutors are students who excelled in your courses, ready to help
            you succeed with their firsthand knowledge and proven expertise.
          </span>
        </div>

        <div className="flex flex-col self-start w-1/4 bg-gray-300 p-8 rounded-3xl">
          <span className="mb-[10px] material-symbols-rounded text-5xl">
            event
          </span>
          <span className="text-black text-2xl font-merriweather_sans">
            Flexible
            <br />
            Timing
          </span>
          <span className="text-gray-700 text-md font-merriweather_sans">
            Learn whenever it suits you. Our tutors offer flexible hours,
            allowing you to schedule sessions around your life and other
            responsibilities.
          </span>
        </div>

        <div className="flex flex-col self-start w-1/4 bg-gray-300 p-8 rounded-3xl">
          <span className="mb-[10px] material-symbols-rounded text-5xl">
            volunteer_activism
          </span>
          <span className="text-black text-2xl font-merriweather_sans">
            Optional
            <br />
            Payment
          </span>
          <span className="text-gray-700 text-md font-merriweather_sans">
            Only pay if you want to! Our donation-based model makes high-quality
            tutoring accessible, with no obligation to pay.
          </span>
        </div>

        <div className="flex flex-col self-start w-1/4 bg-gray-300 p-8 rounded-3xl">
          <span className="mb-[10px] material-symbols-rounded text-5xl">
            group
          </span>
          <span className="text-black text-2xl font-merriweather_sans">
            Tailored
            <br />
            Support
          </span>
          <span className="text-gray-700 text-md font-merriweather_sans">
            Get help that’s customized for you. Our tutors adapt to your
            learning style and pace, ensuring you truly understand each topic.
          </span>
        </div>
      </div>
    </>
  );
};

function Home() {
  const [studentCount, setStudentCount] = useState("...");
  const [tutorCount, setTutorCount] = useState("...");
  const [courseCount, setCourseCount] = useState("...");

  useEffect(() => {
    const fetchStudentCount = async () => {
      try {
        const response = await fetch("http://localhost:8080/students/count");
        if (!response.ok) {
          console.error("Network response was not ok");
        }
        const count = await response.json();
        setStudentCount(count);
      } catch (error) {
        console.error("Failed to fetch student count:", error);
      }
    };
    const fetchTutorCount = async () => {
      try {
        const response = await fetch("http://localhost:8080/tutors/count");
        if (!response.ok) {
          console.error("Network response was not ok");
        }
        const count = await response.json();
        setTutorCount(count);
      } catch (error) {
        console.error("Failed to fetch student count:", error);
      }
    };
    const fetchCourseCount = async () => {
      try {
        const response = await fetch("http://localhost:8080/courses/count");
        if (!response.ok) {
          console.error("Network response was not ok");
        }
        const count = await response.json();
        setCourseCount(count);
      } catch (error) {
        console.error("Failed to fetch student count:", error);
      }
    };

    fetchStudentCount();
    fetchTutorCount();
    fetchCourseCount();
  }, []);

  return (
    <div className="flex flex-col items-center w-full bg-white overflow-hidden">
      <NavBar isLoggedIn={false} currentPage={window.location.pathname} />
      <SearchSection />
      <StatsSection
        students={studentCount}
        courses={courseCount}
        tutors={tutorCount}
      />
      <FeaturesSection />
    </div>
  );
}

export default Home;
