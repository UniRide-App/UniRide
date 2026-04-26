import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";

const firebaseConfig = {
  apiKey: "AIzaSyAV98ue1WLckSxjQz2bhHHgi0SWPRYe2h0",
  authDomain: "uniride-9b20e.firebaseapp.com",
  projectId: "uniride-9b20e",
  storageBucket: "uniride-9b20e.firebasestorage.app",
  messagingSenderId: "735315497526",
  appId: "1:735315497526:web:972fd769a25cfbe0d67e97",
  measurementId: "G-GNN582ZF0H"
};

const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);
export default app;