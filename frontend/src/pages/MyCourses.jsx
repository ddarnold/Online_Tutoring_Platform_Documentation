import React from "react";
import NavBar from "../components/Navbar";

function MyCourses() {
  return <NavBar currentPage={document.location.pathname} />;
}

export default MyCourses;
