import { useNavigate } from "react-router-dom";
import logo from "../assets/logo.png";
import React from "react";

export default function NavBar({ isLoggedIn, currentPage }) {
  const navigate = useNavigate();
  const menuItems = [
    { label: "Home", path: "/" },
    { label: "My Courses", path: "/my-courses" },
    { label: "Tutor Centre", path: "/tutor-centre" },
    { label: "Messages", path: "/messages" },
  ];
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
          {menuItems.map((item) => (
            <button
              key={item.path}
              onClick={() => navigate(item.path)}
              className={`px-4 py-2 text-sm font-merriweather_sans ${
                currentPage === item.path ? "bg-gray-200 rounded-md" : ""
              }`}
            >
              {item.label}
            </button>
          ))}
        </div>
        <div className="flex items-center px-4">
          {isLoggedIn ? (
            <button className="px-4 py-2 text-sm border border-black font-merriweather_sans rounded-full hover:bg-black hover:text-white">
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
