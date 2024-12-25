import React from "react";

const MessageCard = ({
  isReqUserMessage,
  content,
  timestamp,
  profilePic,
  fileType,
  id,
  token,
}) => {
  async function handleCheck() {
    if (fileType) {
      try {
        const response = await fetch(
          `http://localhost:8080/api/messages/download/${id}`,
          {
            method: "GET",
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (!response.ok) {
          throw new Error("Failed to fetch file: " + response.statusText);
        }

        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.style.display = "none";
        a.href = url;
        a.download = `file-${id}`;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
      } catch (error) {
        console.error("Error downloading the file:", error);
      }
    }
  }

  return (
    <div
      className={`flex w-full ${
        isReqUserMessage ? "justify-start" : "justify-end"
      } my-2`}
    >
      {!isReqUserMessage && profilePic && (
        <img
          src={profilePic}
          alt="profile"
          className="w-8 h-8 rounded-full mr-2"
        />
      )}
      <div
        className={`max-w-xs px-4 py-2 rounded-lg ${
          isReqUserMessage
            ? "bg-blue-500 text-white"
            : "bg-gray-200 text-black"
        }`}
      >
        <p>{content}</p>
        {fileType && (
          <button
            onClick={handleCheck}
            className="text-blue-500 underline mt-2 text-sm"
          >
            Download File
          </button>
        )}
        {timestamp && (
          <span className="text-xs text-gray-600 mt-1">
            {new Date(timestamp).toLocaleTimeString()}
          </span>
        )}
      </div>
    </div>
  );
};

export default MessageCard;
