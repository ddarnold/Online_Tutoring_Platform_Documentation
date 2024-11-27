import "./App.css";
import "@smastrom/react-rating/style.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import MyCourses from "./pages/MyCourses";
import TutorCentre from "./pages/TutorCentre";
import Messages from "./pages/Messages";
import Search from "./pages/Search";
import Login from "./pages/Login";
import SignUp from "./pages/SignUp";
import Course from "./pages/Course";
import Tutor from "./pages/Tutor";

export default function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/my-courses" element={<MyCourses />} />
        <Route path="/tutor-centre" element={<TutorCentre />} />
        <Route path="/messages" element={<Messages />} />
        <Route path="/search" element={<Search />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/course" element={<Course />} />
        <Route path="/tutor" element={<Tutor />} />
      </Routes>
    </Router>
  );
}
