import React, { useCallback, useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import NavBar from "../components/Navbar";
import CourseSearchResultItem from "../components/CourseSearchResultItem";
import TutorSearchResultItem from "../components/TutorSearchResultItem";

function Search() {
  const [searchResults, setSearchResults] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const location = useLocation();
  const query = new URLSearchParams(location.search);
  const courseName = query.get("courseName");
  const tutorName = query.get("tutorName");
  const categoryName = query.get("categoryName");

  const fetchSearchResults = useCallback(async () => {
    if (courseName || tutorName || categoryName) {
      try {
        setLoading(true);
        setError(null);

        const queryParams = new URLSearchParams();
        if (courseName) queryParams.append("courseName", courseName);
        if (tutorName) queryParams.append("tutorName", tutorName);
        let response;
        if (categoryName) {
          response = await fetch(
            `http://localhost:8080/search/category/${categoryName}`,
          );
        } else {
          response = await fetch(`http://localhost:8080/search?${queryParams}`);
        }
        const data = await response.json();
        console.log(data);
        if (response.ok) {
          setSearchResults(data);
        } else {
          setError("Error fetching search results.");
        }
      } catch (err) {
        setError("An error occurred while fetching the search results.");
      } finally {
        setLoading(false);
      }
    }
  }, [courseName, tutorName, categoryName]);

  useEffect(() => {
    fetchSearchResults();
  }, [fetchSearchResults]);

  return (
    <div className="flex flex-col items-center w-full bg-white overflow-hidden">
      <NavBar isLoggedIn={false} currentPage="/" />
      {loading && (
        <div className="mt-[150px] font-merriweather_sans text-xl">
          Loading...
        </div>
      )}
      {!loading && error && (
        <div className="mt-[150px] font-merriweather_sans">{error}</div>
      )}
      {!loading && !error && searchResults?.length > 0 && (
        <div className="w-full max-w-4xl mt-[150px]">
          {courseName && (
            <span className="font-merriweather_sans text-xl my-5">
              {searchResults.length} result(s) for '{courseName}'
            </span>
          )}
          {tutorName && (
            <span className="font-merriweather_sans text-xl my-5">
              {searchResults.length} result(s) for '{tutorName}'
            </span>
          )}
          {categoryName && (
            <span className="font-merriweather_sans text-xl my-5">
              {searchResults.length} result(s) for '{categoryName}'
            </span>
          )}
          <ul
            className={
              tutorName
                ? "grid grid-cols-[repeat(auto-fill,_minmax(200px,_1fr))] gap-4"
                : ""
            }
          >
            {searchResults.map((result, index) => {
              if (courseName) {
                return <CourseSearchResultItem course={result} key={index} />;
              }
              if (tutorName) {
                return <TutorSearchResultItem tutor={result} key={index} />;
              }
              if (categoryName) {
                return <CourseSearchResultItem course={result} key={index} />;
              }
              return null;
            })}
          </ul>
        </div>
      )}
      {!loading && !error && searchResults?.length === 0 && (
        <div className="flex flex-col items-center w-full bg-white overflow-hidden mt-[150px]">
          {courseName && (
            <span className="font-merriweather_sans text-xl m-5">
              No results found for '{courseName}'
            </span>
          )}
          {tutorName && (
            <span className="font-merriweather_sans text-xl m-5">
              No results found for '{tutorName}'
            </span>
          )}
          {categoryName && (
            <span className="font-merriweather_sans text-xl m-5">
              No results found for '{categoryName}'
            </span>
          )}
        </div>
      )}
    </div>
  );
}

export default Search;
