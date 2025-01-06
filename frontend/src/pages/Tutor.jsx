import NavBar from "../components/Navbar";
import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { Tooltip } from "react-tooltip";
import calculateAverageRating from "../helpers/CalculateAverageRating";
import CourseSearchResultItem from "../components/CourseSearchResultItem";
import { Rating, StickerStar } from "@smastrom/react-rating";

const ratingStyle = {
  itemShapes: StickerStar,
  activeFillColor: "#1e40af",
  inactiveFillColor: "#bcbcbc",
};

function Tutor() {
  const navigate = useNavigate();
  const location = useLocation();
  const query = new URLSearchParams(location.search);
  const id = query.get("id");
  const [tutor, setTutor] = useState(false);
  const isLoggedIn = false;
  const fetchTutorDetails = async () => {
    const res = await fetch("http://localhost:8080/tutor?id=" + id);
    const data = await res.json();
    console.log(data);
    setTutor(data);
  };
  useEffect(() => {
    fetchTutorDetails();
  });
  return (
    <div className="flex flex-col items-center w-full bg-white overflow-hidden">
      <NavBar currentPage="/" />
      {tutor ? (
        <div className="mt-[120px] w-full max-w-6xl font-merriweather_sans mb-10">
          <div className="flex items-center">
            <div className="flex flex-col w-5/6">
              <div className="inline-flex items-center w-full mt-5">
                <div className="text-4xl">
                  {tutor.firstName} {tutor.lastName}
                </div>
                <span
                  onClick={() => {
                    navigator.clipboard.writeText(window.location.href);
                  }}
                  className="copy_link_anchor_element material-symbols-rounded text-3xl ml-3 cursor-pointer text-blue-600"
                >
                  link
                </span>
              </div>
              <Tooltip
                anchorSelect=".copy_link_anchor_element"
                place="top"
                openOnClick="true"
              >
                Link copied!
              </Tooltip>
              <div className="text-xl w-auto self-start">
                {tutor.isVerified ? (
                  <span className="text-white py-1 px-2 rounded-md font-merriweather_sans bg-blue-600 text-xs inline-flex items-center">
                    Verified
                    <span className="material-symbols-rounded ml-1 text-sm leading-none">
                      verified
                    </span>
                  </span>
                ) : (
                  <span className="text-white py-1 px-2 rounded-md font-merriweather_sans bg-gray-600 text-xs">
                    Unverified
                  </span>
                )}
              </div>
            </div>
            <button
              onClick={() => {
                if (isLoggedIn) {
                  navigate("/");
                }
              }}
              className={
                isLoggedIn
                  ? "bg-blue-800 ml-10 max-h-12 rounded-full text-white py-2 px-4"
                  : "message_anchor_element bg-blue-800 ml-10 max-h-12 rounded-full text-white py-2 px-4"
              }
            >
              Message
            </button>
            <Tooltip
              anchorSelect=".message_anchor_element"
              place="top"
              openOnClick={true}
            >
              Log in first!
            </Tooltip>
          </div>
          <div className="flex">
            <div className="flex flex-col w-3/4 mr-10">
              <div className="mt-5 text-xl text-gray-800">Overview</div>
              <div className="mt-1 text-sm text-gray-600">
                {tutor.tutor_description}
              </div>
              <div className="mt-5 flex text-black">
                {tutor.ratings?.length > 0 ? (
                  <>
                    <div className="font-merriweather_sans flex flex-col items-center bg-gray-200 py-2 px-4 rounded-lg mr-5">
                      <span className="text-sm">Rating</span>
                      <span className="text-md">
                        <strong>
                          {calculateAverageRating(tutor.ratings).toFixed(1)}
                        </strong>
                      </span>
                    </div>
                    <div className="font-merriweather_sans flex flex-col items-center bg-gray-200 py-2 px-4 rounded-lg mr-5">
                      <span className="text-sm">Reviews</span>
                      <span className="text-md">
                        <strong>{tutor.ratings.length}</strong>
                      </span>
                    </div>
                  </>
                ) : (
                  <div className="font-merriweather_sans flex flex-col items-center bg-gray-200 py-2 px-4 rounded-lg mr-5">
                    <span className="text-sm">Rating</span>
                    <span className="text-md">
                      <strong>Not yet rated</strong>
                    </span>
                  </div>
                )}
                <div className="font-merriweather_sans flex flex-col items-center bg-gray-200 py-2 px-4 rounded-lg mr-5">
                  <span className="text-sm">Courses</span>
                  <span className="text-md">
                    <strong>{tutor.courses?.length}</strong>
                  </span>
                </div>
              </div>
              <div className="mt-10 text-2xl text-gray-800">Courses</div>
              {tutor.courses?.map((result, index) => {
                return <CourseSearchResultItem course={result} key={index} />;
              })}
            </div>
            <div className="flex flex-col bg-gray-200 rounded-xl w-1/4 p-4 self-start h-auto">
              <div className="text-xl rounded-md text-black self-start w-auto">
                Reviews
              </div>
              {tutor.ratings?.length > 0 ? (
                tutor.ratings.map((result, index) => {
                  return (
                    <>
                      <div className="text-sm mt-4">
                        {result.student.firstName} {result.student.lastName}
                      </div>
                      <Rating
                        key={index}
                        readOnly={true}
                        style={{ maxWidth: 100 }}
                        value={result.points}
                        itemStyles={ratingStyle}
                      />
                      <div className="text-sm text-gray-600">
                        {result.review}
                      </div>
                    </>
                  );
                })
              ) : (
                <div className="font-merriweather_sans flex flex-col items-center bg-gray-200 p-4">
                  No reviews yet
                </div>
              )}
            </div>
          </div>
        </div>
      ) : (
        <div className="mt-[120px] w-full max-w-6xl font-merriweather_sans text-xl"></div>
      )}
    </div>
  );
}

export default Tutor;
