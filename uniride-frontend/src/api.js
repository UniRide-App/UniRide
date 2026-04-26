import axios from 'axios';
import { getAuth } from 'firebase/auth';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
});

// Attach Firebase ID token to every request automatically
api.interceptors.request.use(async (config) => {
  const user = getAuth().currentUser;
  if (user) {
    const token = await user.getIdToken();
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Auth
export const register = (data) => api.post('/auth/register', data);
export const login = () => api.post('/auth/login');

// User
export const getProfile = (id) => api.get(`/users/${id}`);
export const updateProfile = (id, data) => api.put(`/users/${id}/profile`, data);
export const switchAccountType = (id, data) => api.post(`/users/${id}/account-type`, data);
export const goOnline = (id) => api.post(`/users/${id}/online`);
export const goOffline = (id) => api.post(`/users/${id}/offline`);
export const updateLocation = (id, data) => api.put(`/users/${id}/location`, data);
export const nearbyDrivers = (lat, lng) => api.get(`/users/nearby-drivers?lat=${lat}&lng=${lng}`);

// Rides
export const requestRide = (riderId, data) => api.post(`/rides/request?riderId=${riderId}`, data);
export const acceptRide = (rideId, driverId) => api.post(`/rides/${rideId}/accept?driverId=${driverId}`);
export const driverArrived = (rideId, driverId) => api.post(`/rides/${rideId}/arrived?driverId=${driverId}`);
export const startTrip = (rideId, driverId) => api.post(`/rides/${rideId}/start?driverId=${driverId}`);
export const completeTrip = (rideId, driverId) => api.post(`/rides/${rideId}/complete?driverId=${driverId}`);
export const cancelRide = (rideId, userId, reason) => api.post(`/rides/${rideId}/cancel?userId=${userId}&reason=${encodeURIComponent(reason)}`);
export const rateRide = (rideId, userId, data) => api.post(`/rides/${rideId}/rate?userId=${userId}`, data);
export const getRide = (rideId) => api.get(`/rides/${rideId}`);
export const getPendingRides = () => api.get('/rides/pending');
export const rideHistory = (userId) => api.get(`/rides/history?userId=${userId}`);

// Schedule
export const postSchedule = (userId, data) => api.post(`/schedules/ride?userId=${userId}`, data);
export const postAvailability = (userId, data) => api.post(`/schedules/availability?userId=${userId}`, data);
export const mySchedules = (userId) => api.get(`/schedules/mine?userId=${userId}`);
export const findMatches = (scheduleId) => api.get(`/schedules/${scheduleId}/matches`);
export const acceptMatch = (scheduleId, matchId) => api.post(`/schedules/${scheduleId}/accept-match?matchId=${matchId}`);
export const deleteSchedule = (id, userId) => api.delete(`/schedules/${id}?userId=${userId}`);

// Maps
export const autocomplete = (input) => api.get(`/maps/autocomplete?input=${encodeURIComponent(input)}`);
export const geocodeAddress = (address) => api.get(`/maps/geocode?address=${encodeURIComponent(address)}`);

export default api;
