import { useNavigate } from "react-router-dom";

function TutorSearchResultItem({ tutor }) {
  const navigate = useNavigate();
  return (
    <li
      key={tutor.userId}
      onClick={() => navigate("/tutor?id=" + tutor.userId)}
      className="col-span-2 cursor-pointer flex rounded-2xl shadow-gray-300 shadow-sm bg-gray-900 p-7 mt-1 hover:bg-gray-800"
    >
      <div>
        <div className="inline-flex items-center">
          {tutor.isVerified ? (
            <span className="text-blue-100 py-1 px-2 rounded-sm font-merriweather_sans bg-blue-600 bg-opacity-40 text-xs inline-flex items-center">
              Verified
              <span className="material-symbols-rounded ml-1 text-sm leading-none">
                verified
              </span>
            </span>
          ) : (
            <span className="text-blue-100 py-1 px-2 rounded-sm font-merriweather_sans bg-blue-600 bg-opacity-40 text-xs">
              Unverified
            </span>
          )}
        </div>

        <div className="font-merriweather_sans text-xl mt-[5px] text-white">
          {tutor.firstName} {tutor.lastName}
        </div>
        {tutor.tutorCourses.length > 1 ? (
          <div className="font-merriweather_sans text-sm text-white">
            {tutor.tutorCourses.length} Courses
          </div>
        ) : (
          <div className="font-merriweather_sans text-md text-white">
            {tutor.tutorCourses.length} Course
          </div>
        )}
      </div>
      <div className="flex flex-col justify-center ml-auto items-end font-merriweather_sans text-sm text-white"></div>
    </li>
  );
}

export default TutorSearchResultItem;
