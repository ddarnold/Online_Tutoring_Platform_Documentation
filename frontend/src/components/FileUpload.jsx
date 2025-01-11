import React, { useState } from "react";

const FileUpload = ({ label, id, onChange }) => {
  const [fileName, setFileName] = useState(null);
  //
  const handleFileChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      setFileName(file.name);
      onChange && onChange(file);
    }
  };

  const handleDrop = (event) => {
    event.preventDefault();
    const file = event.dataTransfer.files[0];
    if (file) {
      setFileName(file.name);
      onChange && onChange(file);
    }
  };

  const handleDragOver = (event) => {
    event.preventDefault();
  };

  return (
    <div className="space-y-2">
      <label htmlFor={id} className="block text-sm font-medium text-gray-700">
        {label}
      </label>
      <div
        onDrop={handleDrop}
        onDragOver={handleDragOver}
        className="relative flex items-center justify-center w-full border-2 border-dashed border-blue-300 rounded-md p-4 cursor-pointer hover:border-blue-500 hover:bg-blue-50"
      >
        <input
          type="file"
          id={id}
          accept="image/*"
          onChange={handleFileChange}
          className="absolute inset-0 opacity-0 cursor-pointer"
        />
        {fileName ? (
          <p className="text-gray-600 text-sm">
            Selected file: <span className="font-medium">{fileName}</span>
          </p>
        ) : (
          <p className="text-gray-500 text-sm">
            Drag & drop an image here or{" "}
            <span className="text-blue-500 font-medium">browse</span>
          </p>
        )}
      </div>
    </div>
  );
};

export default FileUpload;
