import React, { useEffect, useState } from "react";
import NavBar from "../components/Navbar";
import CapitalizeFirstLetter from "../helpers/CapitalizeFirstLetter";
import { useAuth } from "../services/AuthContext";
import apiClient from "../services/AxiosConfig";
import { useNavigate } from "react-router-dom";

function Profile() {
  const { user, logout } = useAuth(); // Retrieve user details from the token
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [roles, setRoles] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchProfile = async () => {
      if (!user) return;
      try {
        const { data } = await apiClient.get(`/user/get-user/${user.id}`);
        setRoles(user.roles);
        setProfile(data);
      } catch (err) {
        setError("Failed to fetch profile data.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchProfile();
  }, [user]);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div className="flex flex-col items-center w-full bg-white overflow-hidden">
      <NavBar currentPage="/profile" />
      <div className="flex flex-col w-full max-w-4xl font-merriweather_sans mt-[150px]">
        {profile && (
          <>
            <div className="flex gap-2 mb-2">
              {roles.map((role, index) => (
                <span
                  className="text-white rounded-md text-xs bg-gray-800 py-1 px-2"
                  key={index}
                >
                  {CapitalizeFirstLetter(role.split("_")[1].toLowerCase())}
                </span>
              ))}
            </div>
            <h1 className="text-black text-2xl">
              {profile.firstName} {profile.lastName}
            </h1>
            <h2 className="text-gray-600 text-md">{profile.email}</h2>
            <button
              onClick={() => {
                logout();
                navigate("/login");
              }}
              className="bg-blue-800 self-start py-2 px-4 rounded-full text-white mt-5"
            >
              Log out
            </button>
          </>
        )}
      </div>
    </div>
  );
}

export default Profile;
