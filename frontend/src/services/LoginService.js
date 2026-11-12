import axios from "axios";

const API_URL = "http://localhost:8080/api"; // make sure the compitible with the backend

class LoginService {
  login(credentials) {
    return axios.post(`${API_URL}/login`, credentials);
  }

  //need to add method
  register(studentData) {
    return axios.post(`${API_URL}/register`, studentData);
  }

  // MIDTERM: remove a student account (password is sent in the body, not the URL)
  remove(studentId, password) {
    return axios.delete(`${API_URL}/students/${encodeURIComponent(studentId)}`, {
      data: { password }
    });
  }
}

export default new LoginService();
