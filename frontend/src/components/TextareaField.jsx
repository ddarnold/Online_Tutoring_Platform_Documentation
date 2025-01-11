//import React from "react";

const TextareaField = ({ label, placeholder, value, onChange, name }) => {
  return (
    <div className="mb-4">
      <label className="block text-gray-700 text-sm font-medium mb-2">
        {label}
      </label>
      <textarea
        name={name}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        className="w-full border border-gray-300 rounded-md px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:outline-none"
        rows="5"
      ></textarea>
    </div>
  );
};

export default TextareaField;
