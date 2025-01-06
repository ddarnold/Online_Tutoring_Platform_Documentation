import { useNavigate } from "react-router-dom";
import logo from "../assets/logo.png";
import React from "react";
import * as Auth from "../services/AuthService";
import { useAuth } from "../services/AuthContext";

export default function NavBar({ currentPage }) {
  const navigate = useNavigate();
  const { user } = useAuth(); // Get user details, including roles
  const isLoggedIn = Auth.isAuthenticated();

  // Define menu items for roles
  const roleBasedMenuItems = {
    ROLE_STUDENT: [
      { label: "My Courses", path: "/my-courses" },
      { label: "Messages", path: "/messages" },
    ],
    ROLE_TUTOR: [
      { label: "Tutor Centre", path: "/tutor-centre" },
      { label: "Messages", path: "/messages" },
    ],
  };

  // Calculate role-based menu items
  const menuItems = Array.from(
    new Set(
      user?.roles
        ?.flatMap((role) => roleBasedMenuItems[role.toUpperCase()] || []) // Convert roles to uppercase for matching
        .map((item) => JSON.stringify(item)), // Serialize to avoid duplicates
    ),
  ).map((item) => JSON.parse(item)); // Deserialize back

  // Always include the "Home" menu item
  const defaultMenuItems = [{ label: "Home", path: "/" }, ...menuItems];

  return (
    <div className="flex justify-between items-center w-full bg-white fixed z-50 shadow-md shadow-black/5">
      <div className="flex justify-between items-center w-full bg-white">
        <div
          className="flex items-center m-5 cursor-pointer"
          onClick={() => navigate("/")}
        >
          <img src={logo} alt="Logo" className="h-8 w-auto mr-2" />
          <span className="text-xl font-merriweather_sans">THUtorium</span>
        </div>
        <div className="flex space-x-4">
          {defaultMenuItems.map((item) => (
            <button
              key={item.path}
              onClick={() => navigate(item.path)}
              className={`px-4 py-2 rounded-md text-sm font-merriweather_sans ${
                currentPage === item.path ? "bg-gray-300" : "hover:bg-gray-200"
              }`}
            >
              {item.label}
            </button>
          ))}
        </div>
        <div className="flex items-center px-4">
          {isLoggedIn ? (
            <button
              onClick={() => navigate("/profile")}
              className={`${
                currentPage !== "/profile"
                  ? "hover:bg-black hover:text-white px-4 py-2 text-sm border border-black font-merriweather_sans rounded-full"
                  : "px-4 py-2 text-sm border bg-black text-white font-merriweather_sans rounded-full"
              } `}
            >
              My Account
            </button>
          ) : (
            <div className="flex space-x-2">
              <button
                className="bg-black text-sm border-dark border rounded-full inline-flex items-center justify-center py-2 px-4 text-center font-merriweather_sans text-white"
                onClick={() => navigate("/login")}
              >
                Log In
              </button>
              <button
                onClick={() => navigate("/signup")}
                className="border-black border rounded-full inline-flex items-center justify-center py-2 px-4 text-center text-sm font-merriweather_sans text-black"
              >
                Sign Up
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
