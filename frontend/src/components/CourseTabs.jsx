import React, { useState, useEffect } from "react";
import Reviews from "./Reviews";
import EnrolledStudents from "./EnrolledStudents";
import Meetings from "./Meetings";

const CourseTabs = ({
  visibleTabs = ["reviews", "students", "meetings"],
  ratings,
  enrolledStudents,
  meetings,
  bookedMeetings,
  fetchStudentBookingStatus,
  user,
}) => {
  const [activeTab, setActiveTab] = useState(visibleTabs[0]);

  // Update activeTab when visibleTabs changes
  useEffect(() => {
    if (!visibleTabs.includes(activeTab)) {
      setActiveTab(visibleTabs[0]); // Set the first available tab as active
    }
  }, [visibleTabs, activeTab]);

  return (
    <div className="fixed right-[2%] top-[48%] bottom-[5%] w-[36%] mx-auto p-6 border border-gray-400 bg-gray-900 rounded-3xl max-h-[70vh] flex flex-col">
      {/* Tabs (Fixed at Top) */}
      {visibleTabs.length > 0 && (
        <div
          role="tablist"
          className="flex space-x-2 bg-gray-700 rounded-full p-1"
        >
          {visibleTabs.map((tab) => (
            <button
              key={tab}
              role="tab"
              aria-selected={activeTab === tab}
              className={`flex-1 py-2 px-4 text-center transition-all duration-200 ease-in-out font-medium
                ${
                  activeTab === tab
                    ? "bg-gray-900 text-white rounded-full"
                    : "text-gray-100 hover:bg-gray-600 hover:text-gray-300 rounded-full"
                }`}
              onClick={() => setActiveTab(tab)}
            >
              {tab === "reviews" && "⭐ Reviews"}
              {tab === "students" && "🧑🏼‍🎓 Students"}
              {tab === "meetings" && "📅 Meetings"}
            </button>
          ))}
        </div>
      )}

      {/* Tab Content (Scrollable) */}
      <div className="flex-1 overflow-y-auto scrollbar-hide">
        {activeTab === "reviews" && <Reviews ratings={ratings} />}
        {activeTab === "students" && (
          <EnrolledStudents enrolledStudents={enrolledStudents} />
        )}
        {activeTab === "meetings" && visibleTabs.includes("meetings") && (
          <Meetings
            user={user}
            meetings={meetings}
            bookedMeetings={bookedMeetings}
            fetchStudentBookingStatus={fetchStudentBookingStatus}
          />
        )}
      </div>
    </div>
  );
};

export default CourseTabs;
