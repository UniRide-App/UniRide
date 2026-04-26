import { useState, useEffect, useRef } from "react";
import { GoogleMap, useJsApiLoader, Marker, DirectionsRenderer } from "@react-google-maps/api";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import "./firebase";
import { getAuth, createUserWithEmailAndPassword, signInWithEmailAndPassword, sendEmailVerification, signOut, updatePassword, EmailAuthProvider, reauthenticateWithCredential } from "firebase/auth";
import { register as apiRegister, login as apiLogin, rideHistory, switchAccountType, postSchedule, postAvailability, mySchedules as apiMySchedules, autocomplete as apiAutocomplete, requestRide, acceptRide, completeTrip, cancelRide, rateRide, findMatches as apiFindMatches, acceptMatch as apiAcceptMatch, geocodeAddress, goOnline, goOffline, driverArrived as apiDriverArrived, startTrip as apiStartTrip, updateLocation, deleteSchedule, getPendingRides, updateProfile } from "./api";


const MAPS_KEY = "AIzaSyCyXRZqkNufKDXeGazX_AL35I9Jl2UDIAk";
const LSU = { lat: 30.4133, lng: -91.1800 };
const MAP_DARK = [
  { elementType: "geometry", stylers: [{ color: "#1a2332" }] },
  { elementType: "labels.text.fill", stylers: [{ color: "#8ec3b9" }] },
  { elementType: "labels.text.stroke", stylers: [{ color: "#1a3646" }] },
  { featureType: "road", elementType: "geometry", stylers: [{ color: "#253040" }] },
  { featureType: "road", elementType: "geometry.stroke", stylers: [{ color: "#1a2332" }] },
  { featureType: "water", elementType: "geometry", stylers: [{ color: "#17263c" }] },
  { featureType: "poi", elementType: "geometry", stylers: [{ color: "#1a3528" }] },
  { featureType: "poi", elementType: "labels.text.fill", stylers: [{ color: "#6b9a76" }] },
];

const B = "#4A8FE7", BD = "#2B6BC4", BL = "#6AABE8", BBG = "#E8F1FD";
const RED = "#EF4444", GRN = "#22C55E";
const FN = `'Plus Jakarta Sans',sans-serif`, FD = `'Outfit',sans-serif`;

const TH = {
  light: { bg:"#fff",card:"#F0F4FA",text:"#1A1A2E",t2:"#6B7280",t3:"#9CA3AF",inp:"#EDF1F7",brd:"#E2E8F0",nav:B },
  dark:  { bg:"#0F172A",card:"#1E293B",text:"#F1F5F9",t2:"#94A3B8",t3:"#64748B",inp:"#2D3A4F",brd:"#334155",nav:B },
};

const RIDES_R = [
  { d:"Patrick F. Taylor H", dt:"Today, 2:30 PM", dr:"Marcus", c:5 },
  { d:"LSU Urec", dt:"Yesterday, 4:15 PM", dr:"Sarah", c:7.5 },
  { d:"Tiger Stadium", dt:"Oct 12, 1:00 PM", dr:"James", c:12 },
  { d:"Mike the Tiger's H", dt:"Oct 8, 3:45 PM", dr:"Lisa", c:8.25 },
];
const RIDES_D = [
  { d:"Patrick F. Taylor H", dt:"Today, 2:30 PM", e:5 },
  { d:"LSU Urec", dt:"Yesterday, 4:15 PM", e:7.5 },
  { d:"Tiger Stadium", dt:"Oct 12, 1:00 PM", e:12 },
  { d:"Mike the Tiger's H", dt:"Oct 8, 3:45 PM", e:8.25 },
];
const PLACES = ["Patrick F. Taylor Hall","Student Union","Tiger Stadium","LSU UREC","Middleton Library","The Quad","Alex Box Stadium","PFT Engineering"];

// SVG Icons
const I = {
  car:(c="#fff",s=24)=><svg width={s} height={s} viewBox="0 0 24 24" fill="none"><path d="M7 11l1.5-4h7L17 11M4 11h16c1.1 0 2 .9 2 2v3a1 1 0 01-1 1H3a1 1 0 01-1-1v-3c0-1.1.9-2 2-2z" stroke={c} strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round"/><circle cx="7" cy="17" r="1.5" fill={c}/><circle cx="17" cy="17" r="1.5" fill={c}/></svg>,
  cal:(c="#fff",s=24)=><svg width={s} height={s} viewBox="0 0 24 24" fill="none"><rect x="3" y="4" width="18" height="18" rx="3" stroke={c} strokeWidth="1.6"/><path d="M3 10h18M8 2v4M16 2v4" stroke={c} strokeWidth="1.6" strokeLinecap="round"/></svg>,
  usr:(c="#fff",s=24)=><svg width={s} height={s} viewBox="0 0 24 24" fill="none"><circle cx="12" cy="8" r="4" stroke={c} strokeWidth="1.6"/><path d="M6 21v-1a6 6 0 0112 0v1" stroke={c} strokeWidth="1.6" strokeLinecap="round"/></svg>,
  srch:(c,s=20)=><svg width={s} height={s} viewBox="0 0 24 24" fill="none"><circle cx="11" cy="11" r="7" stroke={c} strokeWidth="2"/><path d="M16.5 16.5L21 21" stroke={c} strokeWidth="2" strokeLinecap="round"/></svg>,
  pin:(c,s=18)=><svg width={s} height={s} viewBox="0 0 24 24" fill="none"><path d="M12 21s-7-5.5-7-10.5a7 7 0 0114 0C19 15.5 12 21 12 21z" stroke={c} strokeWidth="1.6"/><circle cx="12" cy="10.5" r="2.5" fill={c}/></svg>,
  bk:(c,s=24)=><svg width={s} height={s} viewBox="0 0 24 24" fill="none"><path d="M15 18l-6-6 6-6" stroke={c} strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"/></svg>,
  arr:(c,s=20)=><svg width={s} height={s} viewBox="0 0 24 24" fill="none"><path d="M9 6l6 6-6 6" stroke={c} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/></svg>,
  xc:(s=18)=><svg width={s} height={s} viewBox="0 0 24 24" fill="none"><circle cx="12" cy="12" r="10" fill={RED} opacity=".85"/><path d="M15 9l-6 6M9 9l6 6" stroke="#fff" strokeWidth="2" strokeLinecap="round"/></svg>,
  eye:(c,s=20)=><svg width={s} height={s} viewBox="0 0 24 24" fill="none"><path d="M1 12s4-7 11-7 11 7 11 7-4 7-11 7S1 12 1 12z" stroke={c} strokeWidth="1.5"/><circle cx="12" cy="12" r="3" stroke={c} strokeWidth="1.5"/></svg>,
  out:(c="#fff",s=18)=><svg width={s} height={s} viewBox="0 0 24 24" fill="none"><path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4M16 17l5-5-5-5M21 12H9" stroke={c} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/></svg>,
  grad:(c,s=48)=><svg width={s} height={s} viewBox="0 0 48 48" fill="none"><path d="M24 6L4 16l20 10 20-10L24 6z" stroke={c} strokeWidth="2.5"/><path d="M10 20v10c0 4 6 8 14 8s14-4 14-8V20" stroke={c} strokeWidth="2.5"/><path d="M40 16v14" stroke={c} strokeWidth="2.5" strokeLinecap="round"/></svg>,
  edt:()=><svg width="22" height="22" viewBox="0 0 24 24" fill="none"><circle cx="12" cy="12" r="10" fill={B}/><path d="M14 8.5l1.5 1.5L10 15.5H8.5V14L14 8.5z" fill="#fff"/></svg>,
};

// Google Maps component
function MapView({ dk, driverPos, pickup, destination }) {
  const { isLoaded } = useJsApiLoader({ id: "uniride-map", googleMapsApiKey: MAPS_KEY });
  const [userPos, setUserPos] = useState(null);
  const [routeResult, setRouteResult] = useState(null);

  useEffect(() => {
    navigator.geolocation?.getCurrentPosition(
      p => setUserPos({ lat: p.coords.latitude, lng: p.coords.longitude }),
      () => {}
    );
  }, []);

  useEffect(() => {
    if (!isLoaded || !pickup || !destination) { setRouteResult(null); return; }
    const svc = new window.google.maps.DirectionsService();
    svc.route(
      { origin: pickup, destination, travelMode: window.google.maps.TravelMode.DRIVING },
      (result, status) => { if (status === "OK") setRouteResult(result); }
    );
  }, [isLoaded, pickup, destination]);

  if (!isLoaded) return (
    <div style={{ width:"100%", height:"100%", background: dk?"#1a2332":"#e8e4dd", display:"flex", alignItems:"center", justifyContent:"center" }}>
      <span style={{ color: dk?"#4a6a80":"#9b9080", fontSize:13, fontFamily:FN }}>Loading map…</span>
    </div>
  );

  return (
    <GoogleMap
      mapContainerStyle={{ width:"100%", height:"100%" }}
      center={driverPos || userPos || LSU}
      zoom={15}
      options={{ disableDefaultUI:true, clickableIcons:false, styles: dk ? MAP_DARK : [] }}
    >
      {routeResult && (
        <DirectionsRenderer
          directions={routeResult}
          options={{ suppressMarkers: true, polylineOptions: { strokeColor: B, strokeWeight: 4 } }}
        />
      )}
      {userPos && (
        <Marker
          position={userPos}
          icon={{
            path: window.google.maps.SymbolPath.CIRCLE,
            scale: 8,
            fillColor: B,
            fillOpacity: 1,
            strokeColor: "#fff",
            strokeWeight: 2,
          }}
        />
      )}
      {driverPos && (
        <Marker
          position={driverPos}
          icon={{
            path: window.google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
            scale: 6,
            fillColor: "#22C55E",
            fillOpacity: 1,
            strokeColor: "#fff",
            strokeWeight: 2,
          }}
        />
      )}
    </GoogleMap>
  );
}

// ─── Screen wrapper
function S({n, scr, children}) {
  return (
    <div style={{position:"absolute",inset:0,display:"flex",flexDirection:"column",opacity:scr===n?1:0,pointerEvents:scr===n?"auto":"none",transition:"opacity .3s",overflow:"hidden"}}>
      {children}
    </div>
  );
}

// ═══ MAIN APP ═══
export default function UniRide() {
  const [scr,setScr]=useState("welcome");
  const [dk,setDk]=useState(false);
  const [drv,setDrv]=useState(false);
  const [tab,setTab]=useState("rides");
  const [reg,setReg]=useState({f:"",l:"",em:"",ph:"",pw:""});
  const [showPw,setShowPw]=useState(false);
  const [loginData,setLoginData]=useState({em:"",pw:""});
  const [showLoginPw,setShowLoginPw]=useState(false);
  const [verifyEmailSent, setVerifyEmailSent] = useState(false);
  const [prof, setProf]=useState({f:"",l:"",em:"",ph:""});
  const [sq,setSq]=useState("");
  const [showSrch,setShowSrch]=useState(false);
  const [suggestions,setSuggestions]=useState([]);
  const [srchLoading,setSrchLoading]=useState(false);
  const srchDebounce=useRef(null);
  const [calDay,setCalDay]=useState(()=>new Date().getDate());
  const [aTime,setATime]=useState("");
  const [lFrom,setLFrom]=useState("");
  const [gTo,setGTo]=useState("");
  const [authLoading, setAuthLoading] = useState(false);
  const [authError, setAuthError] = useState("");
  const [userId, setUserId] = useState("");
  const [realRides, setRealRides] = useState(null);
  const [ridesLoading, setRidesLoading] = useState(false);
  const [accountLoading, setAccountLoading] = useState(false);
  const [schedLoading, setSchedLoading] = useState(false);
  const [riderSchedules, setRiderSchedules] = useState([]);
  const [riderSchedsLoading, setRiderSchedsLoading] = useState(false);
  const [schedMatches, setSchedMatches] = useState([]);
  const [matchLoading, setMatchLoading] = useState(false);
  const [matchSuccess, setMatchSuccess] = useState("");
  const [lastPostedSchedId, setLastPostedSchedId] = useState("");
  const [calError, setCalError] = useState("");
  const [calSuccess, setCalSuccess] = useState("");
  const [userLoc, setUserLoc] = useState(LSU);
  const [selectedDest, setSelectedDest] = useState(null);
  const [rideId, setRideId] = useState("");
  const [ridePrice, setRidePrice] = useState(null);
  const [rideDriver, setRideDriver] = useState({ name: "Alex K." });
  const [rideEta, setRideEta] = useState(8);
  const [tripSeconds, setTripSeconds] = useState(0);
  const [driverArrived, setDriverArrived] = useState(false);
  const [sosOpen, setSosOpen] = useState(false);
  const [starRating, setStarRating] = useState(0);
  const [rateLoading, setRateLoading] = useState(false);
  const [requestLoading, setRequestLoading] = useState(false);
  const [requestError, setRequestError] = useState("");
  const [driverPos, setDriverPos] = useState(null);
  const [rideDriverId, setRideDriverId] = useState("");
  const [isOnline, setIsOnline] = useState(false);
  const [pendingRides, setPendingRides] = useState([]);
  const [driverRide, setDriverRide] = useState(null);
  const [driverRidePhase, setDriverRidePhase] = useState("");
  const [driverRides, setDriverRides] = useState(null);
  const [editMode, setEditMode] = useState(false);
  const [editProf, setEditProf] = useState({f:"",l:"",ph:""});
  const [profSaving, setProfSaving] = useState(false);
  const [pwModal, setPwModal] = useState(false);
  const [currentPw, setCurrentPw] = useState("");
  const [newPw, setNewPw] = useState("");
  const [pwError, setPwError] = useState("");
  const [pwSuccess, setPwSuccess] = useState("");
  const [pwLoading, setPwLoading] = useState(false);
  const [calYear, setCalYear] = useState(() => new Date().getFullYear());
  const [calMonth, setCalMonth] = useState(() => new Date().getMonth());
  const pendingPollRef = useRef(null);
  const locationIntervalRef = useRef(null);
  const stompRef = useRef(null);
  const rideSubRef = useRef(null);
  const driverLocSubRef = useRef(null);

  const t = dk ? TH.dark : TH.light;

  const formatArrivalTime = (t) => {
    if (!t) return "12:00 PM";
    const [h, m] = (Array.isArray(t) ? t : t.split(":").map(Number));
    const ampm = h >= 12 ? "PM" : "AM";
    return `${h % 12 || 12}:${String(m).padStart(2, "0")} ${ampm}`;
  };

  const formatRideDate = (iso) => {
    if (!iso) return "";
    const d = new Date(iso);
    const now = new Date();
    const yesterday = new Date(now); yesterday.setDate(now.getDate() - 1);
    const time = d.toLocaleTimeString("en-US", { hour: "numeric", minute: "2-digit" });
    if (d.toDateString() === now.toDateString()) return `Today, ${time}`;
    if (d.toDateString() === yesterday.toDateString()) return `Yesterday, ${time}`;
    return `${d.toLocaleDateString("en-US", { month: "short", day: "numeric" })}, ${time}`;
  };

  useEffect(() => {
    if (scr !== "home" || !userId) return;
    setRidesLoading(true);
    rideHistory(userId)
      .then(res => {
        const rides = res.data?.data;
        if (Array.isArray(rides) && rides.length > 0) {
          setRealRides(rides.map(r => ({
            d: r.destinationAddress || "Unknown destination",
            dt: formatRideDate(r.requestedAt),
            dr: r.driverName || "—",
            c: r.price || 0,
          })));
        }
      })
      .catch(() => {/* keep mock fallback */})
      .finally(() => setRidesLoading(false));
  }, [scr, userId]);

  useEffect(() => {
    if (scr !== "home" || !userId || !drv) return;
    rideHistory(userId)
      .then(res => {
        const rides = res.data?.data;
        if (Array.isArray(rides) && rides.length > 0) {
          setDriverRides(rides.filter(r => r.driverId === userId).map(r => ({
            d: r.destinationAddress || "Unknown destination",
            dt: formatRideDate(r.requestedAt),
            e: r.driverEarnings || r.price || 0,
          })));
        }
      })
      .catch(() => {});
  }, [scr, userId, drv]);

  useEffect(() => {
    if (scr !== "schedWeek" || !userId) return;
    setRiderSchedsLoading(true);
    apiMySchedules(userId)
      .then(res => {
        const schedules = res.data?.data;
        if (Array.isArray(schedules)) {
          setRiderSchedules(schedules.map(s => ({
            date: s.date || "",
            arrivalTime: s.arrivalTime || "",
            leavingFrom: s.leavingFrom || "",
            goingTo: s.goingTo || "",
            id: s.id,
          })));
        }
      })
      .catch(() => {})
      .finally(() => setRiderSchedsLoading(false));
  }, [scr, userId]);

  useEffect(() => {
    navigator.geolocation?.getCurrentPosition(
      p => setUserLoc({ lat: p.coords.latitude, lng: p.coords.longitude }),
      () => {}
    );
  }, []);

  useEffect(() => {
    if (scr !== "inRide") {
      setTripSeconds(0);
      setDriverArrived(false);
      return;
    }
    const timer = setInterval(() => setTripSeconds(s => s + 1), 1000);
    return () => clearInterval(timer);
  }, [scr]);

  useEffect(() => {
    if (scr !== "findingDriver" || !rideId || rideId.startsWith("mock-")) return;
    // Subscribe via WebSocket for real driver acceptance
    const trySubscribe = () => {
      if (stompRef.current?.connected) {
        subscribeToRide(rideId);
      } else {
        // Retry once WS connects (connection is fast on localhost)
        const poll = setInterval(() => {
          if (stompRef.current?.connected) {
            clearInterval(poll);
            subscribeToRide(rideId);
          }
        }, 200);
        return () => clearInterval(poll);
      }
    };
    const cleanup = trySubscribe();
    return () => {
      cleanup?.();
      rideSubRef.current?.unsubscribe();
    };
  }, [scr, rideId]);

  const MONTH_NAMES = ["January","February","March","April","May","June","July","August","September","October","November","December"];
  const buildCalWeeks = (year, month) => {
    const dim = new Date(year, month + 1, 0).getDate();
    const fd = new Date(year, month, 1).getDay();
    const cells = [...Array(fd).fill(0), ...Array.from({length:dim},(_,i)=>i+1)];
    while (cells.length % 7 !== 0) cells.push(0);
    const wks = [];
    for (let i = 0; i < cells.length; i += 7) wks.push(cells.slice(i, i + 7));
    return wks;
  };
  const isPastDay = (d) => {
    if (!d) return false;
    const now = new Date(); now.setHours(0,0,0,0);
    return new Date(calYear, calMonth, d) < now;
  };
  const prevMonth = () => { setCalDay(1); if (calMonth === 0) { setCalYear(y=>y-1); setCalMonth(11); } else setCalMonth(m=>m-1); };
  const nextMonth = () => { setCalDay(1); if (calMonth === 11) { setCalYear(y=>y+1); setCalMonth(0); } else setCalMonth(m=>m+1); };

  const handlePostSchedule = async () => {
    if (!aTime || !lFrom || !gTo) {
      setCalError("Please fill in arrival time, leaving from, and going to.");
      return;
    }
    setCalError("");
    setCalSuccess("");
    setSchedLoading(true);
    const date = `${calYear}-${String(calMonth + 1).padStart(2, "0")}-${String(calDay).padStart(2, "0")}`;
    const dayOfWeek = new Date(calYear, calMonth, calDay).toLocaleDateString("en-US", { weekday: "long" });
    const body = { date, arrivalTime: aTime, leavingFrom: lFrom, goingTo: gTo, dayOfWeek, recurring: false };
    try {
      const res = await (drv ? postAvailability : postSchedule)(userId, body);
      const newSched = res.data?.data;
      setCalSuccess(drv ? "Availability posted!" : "Schedule posted!");
      if (!drv) {
        setRiderSchedules(prev => [...prev, { date, arrivalTime: aTime, leavingFrom: lFrom, goingTo: gTo, id: newSched?.id }]);
        setSchedMatches([]);
        setMatchSuccess("");
        if (newSched?.id) {
          setLastPostedSchedId(newSched.id);
          setMatchLoading(true);
          try {
            const mRes = await apiFindMatches(newSched.id);
            setSchedMatches(mRes.data?.data || []);
          } catch { setSchedMatches([]); }
          finally { setMatchLoading(false); }
        }
      }
      setATime(""); setLFrom(""); setGTo("");
      setTimeout(() => setCalSuccess(""), 4000);
    } catch (err) {
      setCalError(err.response?.data?.message || "Failed to post. Please try again.");
    } finally {
      setSchedLoading(false);
    }
  };

  const handleSwitchAccountType = async (isDriver) => {
    if (isDriver === drv) return;
    setDrv(isDriver);
    if (!userId || accountLoading) return;
    setAccountLoading(true);
    try {
      await switchAccountType(userId, { accountType: isDriver ? "DRIVER" : "PASSENGER" });
    } catch {
      setDrv(!isDriver);
    } finally {
      setAccountLoading(false);
    }
  };

  const subscribeToDriverLocation = (driverId) => {
    if (!stompRef.current?.connected || !driverId) return;
    driverLocSubRef.current?.unsubscribe();
    driverLocSubRef.current = stompRef.current.subscribe(
      `/topic/driver/${driverId}/location`,
      (msg) => {
        const loc = JSON.parse(msg.body);
        setDriverPos({ lat: loc.latitude, lng: loc.longitude });
      }
    );
  };

  const handleRideWsUpdate = (ride) => {
    const status = ride.status;
    if (status === "ACCEPTED") {
      if (ride.driverName) setRideDriver({ name: ride.driverName });
      if (ride.estimatedDurationMinutes) setRideEta(ride.estimatedDurationMinutes);
      if (ride.driverId) {
        setRideDriverId(ride.driverId);
        subscribeToDriverLocation(ride.driverId);
      }
      go("inRide");
    } else if (status === "DRIVER_ARRIVED") {
      setDriverArrived(true);
    } else if (status === "COMPLETED") {
      go("rateRide");
    } else if (status === "CANCELLED") {
      rideSubRef.current?.unsubscribe();
      driverLocSubRef.current?.unsubscribe();
      setDriverPos(null);
      go("home", "rides");
    }
  };

  const subscribeToRide = (id) => {
    if (!stompRef.current?.connected || !id) return;
    rideSubRef.current?.unsubscribe();
    rideSubRef.current = stompRef.current.subscribe(
      `/topic/ride/${id}`,
      (msg) => handleRideWsUpdate(JSON.parse(msg.body))
    );
  };

  const connectWS = () => {
    if (stompRef.current?.active) return;
    const client = new Client({
      webSocketFactory: () => new SockJS("http://localhost:8080/ws"),
      reconnectDelay: 5000,
      onConnect: () => {
        console.log("[UniRide] WebSocket connected");
      },
      onDisconnect: () => {
        console.log("[UniRide] WebSocket disconnected");
      },
    });
    client.activate();
    stompRef.current = client;
  };

  const disconnectWS = () => {
    rideSubRef.current?.unsubscribe();
    driverLocSubRef.current?.unsubscribe();
    stompRef.current?.deactivate();
    stompRef.current = null;
  };

  const handleSignOut = async () => {
    clearInterval(pendingPollRef.current);
    clearInterval(locationIntervalRef.current);
    disconnectWS();
    try { await signOut(getAuth()); } catch {}
    setUserId("");
    setProf({ f: "", l: "", em: "", ph: "" });
    setReg({ f: "", l: "", em: "", ph: "", pw: "" });
    setLoginData({ em: "", pw: "" });
    setAuthError("");
    setVerifyEmailSent(false);
    setRealRides(null);
    setDrv(false);
    setTab("rides");
    setDriverPos(null);
    setRideDriverId("");
    setIsOnline(false);
    setPendingRides([]);
    setDriverRide(null);
    setDriverRidePhase("");
    setDriverRides(null);
    setEditMode(false);
    go("welcome");
  };

  const selectDestination = async (address) => {
    closeSearch();
    setSelectedDest({ address });
    setRidePrice(6.50);
    go("ridePreview");
    try {
      const res = await geocodeAddress(address);
      const loc = res.data?.results?.[0]?.geometry?.location;
      if (loc) setSelectedDest({ address, lat: loc.lat, lng: loc.lng });
    } catch {}
  };

  const closeSearch = () => {
    clearTimeout(srchDebounce.current);
    setShowSrch(false);
    setSq("");
    setSuggestions([]);
    setSrchLoading(false);
  };

  const handleRegister = async () => {
    if (!reg.f || !reg.l || !reg.em || !reg.ph || !reg.pw) {
      setAuthError("Please fill in all fields.");
      return;
    }
    if (!reg.em.endsWith("@lsu.edu")) {
      setAuthError("Please use your @lsu.edu email address.");
      return;
    }
    setAuthError("");
    setAuthLoading(true);
    try {
      const auth = getAuth();
      const cred = await createUserWithEmailAndPassword(auth, reg.em, reg.pw);
      await sendEmailVerification(cred.user);
      // Create MongoDB profile via backend (token auto-attached by api.js interceptor)
      await apiRegister({ firstName: reg.f, lastName: reg.l, phoneNumber: reg.ph });
      setVerifyEmailSent(true);
      go("verifyEmail");
    } catch (err) {
      const msg = err.code === "auth/email-already-in-use"
        ? "An account with this email already exists."
        : err.response?.data?.message || err.message || "Registration failed. Please try again.";
      setAuthError(msg);
    } finally {
      setAuthLoading(false);
    }
  };

  const handleResendVerification = async () => {
    setAuthError("");
    try {
      const user = getAuth().currentUser;
      if (user) await sendEmailVerification(user);
      setVerifyEmailSent(true);
      setTimeout(() => setVerifyEmailSent(false), 4000);
    } catch {
      setAuthError("Could not resend verification email. Please try again.");
    }
  };

  const handleLogin = async () => {
    if (!loginData.em || !loginData.pw) {
      setAuthError("Please fill in all fields.");
      return;
    }
    setAuthError("");
    setAuthLoading(true);
    try {
      const auth = getAuth();
      const cred = await signInWithEmailAndPassword(auth, loginData.em, loginData.pw);
      if (!cred.user.emailVerified) {
        go("verifyEmail");
        return;
      }
      const res = await apiLogin();
      const user = res.data?.data;
      setUserId(user?.id || "");
      setDrv(user?.accountType === "DRIVER");
      setProf({ f: user?.firstName || "", l: user?.lastName || "", em: user?.email || "", ph: user?.phoneNumber || "" });
      connectWS();
      go("home", "rides");
    } catch (err) {
      const msg = err.code === "auth/invalid-credential" || err.code === "auth/wrong-password"
        ? "Incorrect email or password."
        : err.code === "auth/user-not-found"
        ? "No account found with that email."
        : err.response?.data?.message || err.message || "Login failed. Please try again.";
      setAuthError(msg);
    } finally {
      setAuthLoading(false);
    }
  };

  const handleRequestRide = async () => {
    setRequestError("");
    setRequestLoading(true);
    const destLoc = selectedDest?.lat
      ? { latitude: selectedDest.lat, longitude: selectedDest.lng }
      : { latitude: userLoc.lat + 0.008, longitude: userLoc.lng + 0.006 };
    try {
      const res = await requestRide(userId, {
        pickupAddress: "Current Location",
        pickupLocation: { latitude: userLoc.lat, longitude: userLoc.lng },
        destinationAddress: selectedDest.address,
        destinationLocation: destLoc,
      });
      const ride = res.data?.data;
      setRideId(ride?.id || "mock-" + Date.now());
      setRidePrice(ride?.price ?? 6.50);
      if (ride?.estimatedDurationMinutes) setRideEta(ride.estimatedDurationMinutes);
    } catch {
      setRideId("mock-" + Date.now());
      setRidePrice(6.50);
    } finally {
      setRequestLoading(false);
    }
    go("findingDriver");
  };

  const handleCancelRide = async () => {
    try {
      if (rideId && !rideId.startsWith("mock-")) await cancelRide(rideId, userId, "Rider cancelled");
    } catch {}
    setRideId(""); setRidePrice(null); setTripSeconds(0); setSosOpen(false);
    go("home", "rides");
  };

  const handleCompleteTrip = async () => {
    try {
      if (rideId && !rideId.startsWith("mock-") && rideDriverId) await completeTrip(rideId, rideDriverId);
    } catch {}
    go("rateRide");
  };

  const handleRateRide = async () => {
    if (!starRating) return;
    setRateLoading(true);
    try {
      if (rideId && !rideId.startsWith("mock-")) await rateRide(rideId, userId, { stars: starRating, review: "" });
    } catch {}
    setRateLoading(false);
    setRideId(""); setRidePrice(null); setStarRating(0); setTripSeconds(0);
    go("home", "rides");
  };

  const handleGoOnline = async () => {
    if (!userId) return;
    try {
      await goOnline(userId);
      setIsOnline(true);
      const res = await getPendingRides();
      setPendingRides(res.data?.data || []);
      pendingPollRef.current = setInterval(async () => {
        try { const r = await getPendingRides(); setPendingRides(r.data?.data || []); } catch {}
      }, 5000);
      locationIntervalRef.current = setInterval(() => {
        navigator.geolocation?.getCurrentPosition(p => {
          updateLocation(userId, { latitude: p.coords.latitude, longitude: p.coords.longitude }).catch(()=>{});
        }, ()=>{});
      }, 10000);
    } catch {}
  };

  const handleGoOffline = async () => {
    if (!userId) return;
    try { await goOffline(userId); } catch {}
    setIsOnline(false);
    clearInterval(pendingPollRef.current);
    clearInterval(locationIntervalRef.current);
    setPendingRides([]);
  };

  const handleAcceptRide = async (ride) => {
    try {
      await acceptRide(ride.id, userId);
      setDriverRide(ride);
      setDriverRidePhase("ACCEPTED");
      clearInterval(pendingPollRef.current);
      setPendingRides([]);
      go("driverActiveRide");
    } catch {}
  };

  const handleDriverArrived = async () => {
    if (!driverRide) return;
    try { await apiDriverArrived(driverRide.id, userId); setDriverRidePhase("DRIVER_ARRIVED"); } catch {}
  };

  const handleDriverStartTrip = async () => {
    if (!driverRide) return;
    try { await apiStartTrip(driverRide.id, userId); setDriverRidePhase("IN_PROGRESS"); } catch {}
  };

  const handleDriverCompleteTrip = async () => {
    if (!driverRide) return;
    try { await completeTrip(driverRide.id, userId); } catch {}
    setDriverRide(null);
    setDriverRidePhase("");
    if (isOnline) {
      pendingPollRef.current = setInterval(async () => {
        try { const r = await getPendingRides(); setPendingRides(r.data?.data || []); } catch {}
      }, 5000);
    }
    go("home", "rides");
  };

  const handleEditProfile = () => { setEditProf({f:prof.f,l:prof.l,ph:prof.ph}); setEditMode(true); };
  const handleSaveProfile = async () => {
    setProfSaving(true);
    try {
      const res = await updateProfile(userId, { firstName: editProf.f, lastName: editProf.l, phoneNumber: editProf.ph });
      const u = res.data?.data;
      setProf({ f: u?.firstName||editProf.f, l: u?.lastName||editProf.l, em: prof.em, ph: u?.phoneNumber||editProf.ph });
      setEditMode(false);
    } catch {
      setProf({ ...prof, f: editProf.f, l: editProf.l, ph: editProf.ph });
      setEditMode(false);
    } finally { setProfSaving(false); }
  };

  const handleChangePassword = async () => {
    if (!currentPw) { setPwError("Please enter your current password."); return; }
    if (!newPw || newPw.length < 6) { setPwError("New password must be at least 6 characters."); return; }
    setPwLoading(true); setPwError("");
    try {
      const auth = getAuth();
      const user = auth.currentUser;
      const cred = EmailAuthProvider.credential(user.email, currentPw);
      await reauthenticateWithCredential(user, cred);
      await updatePassword(user, newPw);
      setPwSuccess("Password changed successfully!");
      setCurrentPw(""); setNewPw("");
      setTimeout(() => { setPwModal(false); setPwSuccess(""); }, 1800);
    } catch (err) {
      setPwError(err.code === "auth/wrong-password" || err.code === "auth/invalid-credential" ? "Current password is incorrect." : err.message || "Failed to change password.");
    } finally { setPwLoading(false); }
  };

  const greet = () => { const h=new Date().getHours(); return h<12?"Good morning":h<17?"Good afternoon":"Good evening"; };
  const go = (s,tb) => { setScr(s); if(tb) setTab(tb); };
  const formatTimer = s => `${Math.floor(s/60)}:${String(s%60).padStart(2,"0")}`;


  const inp = (extra={}) => ({
    width:"100%",padding:"14px 16px",fontSize:15,fontFamily:FN,
    background:t.inp,border:`1.5px solid ${t.brd}`,borderRadius:12,
    outline:"none",color:t.text,boxSizing:"border-box",...extra,
  });

  // ─── Bottom Nav ───
  const Nav = ({bg}) => (
    <div style={{display:"flex",justifyContent:"space-around",alignItems:"center",padding:"10px 0 18px",background:bg||t.nav,borderRadius:100,margin:"8px 16px 12px"}}>
      {[["rides",I.car,"home"],["schedule",I.cal,drv?"driverCal":"schedWeek"],["profile",I.usr,"account"]].map(([id,ic,s])=>(
        <button key={id} onClick={()=>go(s,id)} style={{background:"none",border:"none",cursor:"pointer",padding:"8px 24px",borderRadius:100,opacity:tab===id?1:.55}}>
          {ic("#fff",26)}
        </button>
      ))}
    </div>
  );

  // ═══════════════════════════════════════════
  // 1. WELCOME
  // ═══════════════════════════════════════════
  const Welcome = () => (
    <S n="welcome" scr={scr}>
      <div style={{flex:1,display:"flex",flexDirection:"column",alignItems:"center",justifyContent:"center",padding:"40px 32px",background:`linear-gradient(180deg,${B} 0%,#2563EB 50%,#1E40AF 100%)`}}>
        {/* Logo */}
        <div style={{width:88,height:88,borderRadius:22,background:"#fff",display:"flex",alignItems:"center",justifyContent:"center",marginBottom:20,boxShadow:"0 12px 40px rgba(0,0,0,.15)"}}>
          <svg width="48" height="48" viewBox="0 0 48 48" fill="none">
            <path d="M24 8L6 18l18 10 18-10L24 8z" stroke={B} strokeWidth="2.5"/>
            <path d="M12 22v8c0 3.5 5 7 12 7s12-3.5 12-7v-8" stroke={B} strokeWidth="2.5"/>
            <circle cx="24" cy="34" r="3.5" fill={B}/>
            <path d="M21 34l3 3 3-3" stroke="#fff" strokeWidth="1.5" strokeLinecap="round"/>
          </svg>
        </div>
        <h1 style={{fontFamily:FD,fontSize:44,fontWeight:800,color:"#fff",margin:"0 0 10px",letterSpacing:"-.02em"}}>UniRide</h1>
        <p style={{color:"rgba(255,255,255,.7)",fontSize:16,textAlign:"center",margin:"0 0 56px",lineHeight:1.5}}>The smartest way to share<br/>rides across campus.</p>

        <button onClick={()=>go("register")} style={{width:"100%",padding:"18px 28px",background:"#fff",border:"none",borderRadius:100,fontSize:18,fontWeight:700,color:B,fontFamily:FD,cursor:"pointer",display:"flex",alignItems:"center",justifyContent:"space-between",boxShadow:"0 8px 32px rgba(0,0,0,.15)"}}>
          Get Started
          <span style={{width:36,height:36,borderRadius:"50%",background:BBG,display:"flex",alignItems:"center",justifyContent:"center"}}>{I.arr(B,18)}</span>
        </button>

        <button onClick={()=>go("login")} style={{width:"100%",padding:"16px",background:"rgba(255,255,255,.12)",border:"1.5px solid rgba(255,255,255,.3)",borderRadius:100,fontSize:17,fontWeight:600,color:"#fff",fontFamily:FD,cursor:"pointer",marginTop:14}}>Log In</button>
        <p style={{color:"rgba(255,255,255,.4)",fontSize:12,marginTop:24,textAlign:"center"}}>By continuing, you agree to our Terms & Privacy Policy</p>
      </div>
    </S>
  );

  // ═══════════════════════════════════════════
  // 2. REGISTER
  // ═══════════════════════════════════════════
  const Register = () => (
    <S n="register" scr={scr}>
      <div style={{background:`linear-gradient(135deg,${B},${BD})`,color:"#fff",padding:"40px 24px 24px"}}>
        <button onClick={()=>go("welcome")} style={{background:"none",border:"none",cursor:"pointer",padding:0,marginBottom:12}}>{I.bk("#fff",26)}</button>
        <h1 style={{fontFamily:FD,fontSize:30,fontWeight:800,margin:"0 0 6px"}}>Register</h1>
        <p style={{margin:0,fontSize:14,opacity:.8}}>Already have an account? <span onClick={()=>go("welcome")} style={{textDecoration:"underline",cursor:"pointer",fontWeight:600}}>Sign in</span></p>
      </div>
      <div style={{flex:1,padding:"28px 24px",overflow:"auto",background:t.bg,minHeight:0}}>
        <label style={{fontSize:13,fontWeight:600,color:t.t2,display:"block",marginBottom:8}}>First & Last Name</label>
        <div style={{display:"flex",gap:12,marginBottom:20}}>
          <input placeholder="First Name" value={reg.f} onChange={e=>setReg({...reg,f:e.target.value})} style={inp()}/>
          <input placeholder="Last Name" value={reg.l} onChange={e=>setReg({...reg,l:e.target.value})} style={inp()}/>
        </div>
        <label style={{fontSize:13,fontWeight:600,color:t.t2,display:"block",marginBottom:8}}>edu email</label>
        <input placeholder="student email" value={reg.em} onChange={e=>setReg({...reg,em:e.target.value})} style={inp({marginBottom:20})}/>
        <label style={{fontSize:13,fontWeight:600,color:t.t2,display:"block",marginBottom:8}}>phone number</label>
        <input placeholder="phone number" value={reg.ph} onChange={e=>setReg({...reg,ph:e.target.value})} style={inp({marginBottom:20})}/>
        <label style={{fontSize:13,fontWeight:600,color:t.t2,display:"block",marginBottom:8}}>set password</label>
        <div style={{position:"relative",marginBottom:28}}>
          <input placeholder="set password" type={showPw?"text":"password"} value={reg.pw} onChange={e=>setReg({...reg,pw:e.target.value})} style={inp()}/>
          <button onClick={()=>setShowPw(!showPw)} style={{position:"absolute",right:12,top:"50%",transform:"translateY(-50%)",background:"none",border:"none",cursor:"pointer"}}>{I.eye(t.t3)}</button>
        </div>
        {authError&&scr==="register"&&<p style={{color:RED,fontSize:13,fontWeight:600,margin:"0 0 12px",textAlign:"center"}}>{authError}</p>}
        <button onClick={handleRegister} disabled={authLoading} style={{width:"100%",padding:"16px",background:authLoading?"#9CA3AF":BL,border:"none",borderRadius:14,fontSize:18,fontWeight:700,color:"#fff",fontFamily:FD,cursor:authLoading?"not-allowed":"pointer"}}>
          {authLoading?"Creating account…":"Register"}
        </button>
      </div>
    </S>
  );

  // ═══════════════════════════════════════════
  // 2b. LOGIN
  // ═══════════════════════════════════════════
  const Login = () => (
    <S n="login" scr={scr}>
      <div style={{background:`linear-gradient(135deg,${B},${BD})`,color:"#fff",padding:"40px 24px 24px"}}>
        <button onClick={()=>go("welcome")} style={{background:"none",border:"none",cursor:"pointer",padding:0,marginBottom:12}}>{I.bk("#fff",26)}</button>
        <h1 style={{fontFamily:FD,fontSize:30,fontWeight:800,margin:"0 0 6px"}}>Log In</h1>
        <p style={{margin:0,fontSize:14,opacity:.8}}>Don't have an account? <span onClick={()=>go("register")} style={{textDecoration:"underline",cursor:"pointer",fontWeight:600}}>Register</span></p>
      </div>
      <div style={{flex:1,padding:"28px 24px",overflow:"auto",background:t.bg,minHeight:0}}>
        <label style={{fontSize:13,fontWeight:600,color:t.t2,display:"block",marginBottom:8}}>LSU Email</label>
        <input placeholder="student@lsu.edu" value={loginData.em} onChange={e=>setLoginData({...loginData,em:e.target.value})} style={inp({marginBottom:20})}/>
        <label style={{fontSize:13,fontWeight:600,color:t.t2,display:"block",marginBottom:8}}>Password</label>
        <div style={{position:"relative",marginBottom:28}}>
          <input placeholder="password" type={showLoginPw?"text":"password"} value={loginData.pw} onChange={e=>setLoginData({...loginData,pw:e.target.value})} style={inp()}/>
          <button onClick={()=>setShowLoginPw(!showLoginPw)} style={{position:"absolute",right:12,top:"50%",transform:"translateY(-50%)",background:"none",border:"none",cursor:"pointer"}}>{I.eye(t.t3)}</button>
        </div>
        {authError&&scr==="login"&&<p style={{color:RED,fontSize:13,fontWeight:600,margin:"0 0 12px",textAlign:"center"}}>{authError}</p>}
        <button onClick={handleLogin} disabled={authLoading} style={{width:"100%",padding:"16px",background:authLoading?"#9CA3AF":BL,border:"none",borderRadius:14,fontSize:18,fontWeight:700,color:"#fff",fontFamily:FD,cursor:authLoading?"not-allowed":"pointer"}}>
          {authLoading?"Logging in…":"Log In"}
        </button>
      </div>
    </S>
  );

  // ═══════════════════════════════════════════
  // 3. EMAIL VERIFICATION PENDING
  // ═══════════════════════════════════════════
  const VerifyEmail = () => (
    <S n="verifyEmail" scr={scr}>
      <div style={{flex:1,display:"flex",flexDirection:"column",alignItems:"center",justifyContent:"center",padding:"40px 32px",background:t.bg}}>
        <div style={{width:88,height:88,borderRadius:"50%",background:t.card,display:"flex",alignItems:"center",justifyContent:"center",marginBottom:28}}>
          {I.grad(B)}
        </div>
        <h2 style={{fontFamily:FD,fontSize:24,fontWeight:700,color:B,margin:"0 0 10px",textAlign:"center"}}>Check Your Email</h2>
        <p style={{color:t.t2,fontSize:14,margin:"0 0 8px",textAlign:"center"}}>We sent a verification link to</p>
        <p style={{color:t.text,fontSize:15,fontWeight:600,margin:"0 0 32px",textAlign:"center"}}>{reg.em}</p>
        <p style={{color:t.t2,fontSize:13,textAlign:"center",margin:"0 0 24px"}}>Click the link in the email to verify your account, then log in below.</p>
        {authError&&<p style={{color:RED,fontSize:13,fontWeight:600,margin:"0 0 12px",textAlign:"center"}}>{authError}</p>}
        {verifyEmailSent&&<p style={{color:GRN,fontSize:13,fontWeight:600,margin:"0 0 12px",textAlign:"center"}}>Verification email resent!</p>}
        <button onClick={handleResendVerification} style={{marginBottom:16,padding:"12px 32px",background:"none",border:`1.5px solid ${B}`,borderRadius:14,color:B,fontSize:15,fontWeight:600,fontFamily:FD,cursor:"pointer"}}>
          Resend Email
        </button>
        <button onClick={()=>{setAuthError("");go("login");}} style={{padding:"14px 48px",background:B,border:"none",borderRadius:14,color:"#fff",fontSize:16,fontWeight:600,fontFamily:FD,cursor:"pointer"}}>
          Go to Login
        </button>
      </div>
    </S>
  );

  // ═══════════════════════════════════════════
  // 4. RIDER HOME
  // ═══════════════════════════════════════════
  const RiderHome = () => (
    <S n="home" scr={scr}>
      {!drv ? (
        <div style={{height:"100%",display:"flex",flexDirection:"column",overflow:"hidden",background:t.bg}}>
          <div style={{flexShrink:0,background:`linear-gradient(135deg,${B},${BD})`,color:"#fff",padding:"40px 24px 20px"}}>
            <h1 style={{fontFamily:FD,fontSize:28,fontWeight:800,margin:"0 0 16px",lineHeight:1.2}}>{greet()},<br/>Rider</h1>
            <div onClick={()=>setShowSrch(true)} style={{display:"flex",alignItems:"center",gap:10,background:"#fff",borderRadius:100,padding:"12px 18px",cursor:"pointer"}}>
              {I.srch("#9CA3AF",20)}<span style={{color:"#9CA3AF",fontSize:15}}>Where are you going?</span>
            </div>
          </div>
          <div style={{flex:1,overflowY:"auto",WebkitOverflowScrolling:"touch",padding:"20px 20px 0"}}>
            <h3 style={{fontFamily:FD,fontSize:18,fontWeight:700,color:t.text,margin:"0 0 10px"}}>Location</h3>
            <div style={{height:150,borderRadius:16,overflow:"hidden",marginBottom:20}}>
              <MapView dk={dk}/>
            </div>
            <h3 style={{fontFamily:FD,fontSize:18,fontWeight:700,color:t.text,margin:"0 0 12px"}}>Recent Rides</h3>
            {ridesLoading ? (
              [0,1,2].map(i=>(
                <div key={i} style={{background:t.card,borderRadius:14,padding:"14px 16px",display:"flex",alignItems:"center",gap:12,marginBottom:10,opacity:.5}}>
                  <div style={{width:40,height:40,borderRadius:"50%",background:t.brd,flexShrink:0}}/>
                  <div style={{flex:1}}>
                    <div style={{height:14,borderRadius:6,background:t.brd,marginBottom:8,width:"60%"}}/>
                    <div style={{height:11,borderRadius:6,background:t.brd,width:"40%"}}/>
                  </div>
                </div>
              ))
            ) : (realRides ?? RIDES_R).map((r,i)=>(
              <div key={i} style={{background:t.card,borderRadius:14,padding:"14px 16px",display:"flex",alignItems:"center",gap:12,marginBottom:10}}>
                <div style={{width:40,height:40,borderRadius:"50%",background:BBG,display:"flex",alignItems:"center",justifyContent:"center",flexShrink:0}}>{I.pin(B)}</div>
                <div style={{flex:1,minWidth:0}}>
                  <p style={{fontWeight:600,fontSize:15,color:t.text,margin:0,overflow:"hidden",textOverflow:"ellipsis",whiteSpace:"nowrap"}}>{r.d}</p>
                  <p style={{fontSize:12,color:t.t2,margin:"2px 0 0"}}>{r.dt}{r.dr&&r.dr!=="—"?` • Driver: ${r.dr}`:""}</p>
                </div>
                <button onClick={()=>{setSelectedDest({address:r.d});setRidePrice(6.50);go("ridePreview");}} style={{background:B,border:"none",borderRadius:100,padding:"8px 14px",color:"#fff",fontSize:12,fontWeight:600,cursor:"pointer",fontFamily:FN,whiteSpace:"nowrap"}}>Order again</button>
              </div>
            ))}
            <div style={{height:20}}/>
          </div>
          <div style={{flexShrink:0}}><Nav/></div>
        </div>
      ) : (
        // ═══ DRIVER HOME ═══
        <div style={{height:"100%",display:"flex",flexDirection:"column",overflow:"hidden",background:t.bg}}>
          <div style={{flexShrink:0,background:`linear-gradient(135deg,${B},${BD})`,color:"#fff",padding:"44px 28px 20px"}}>
            <h1 style={{fontFamily:FD,fontSize:28,fontWeight:800,color:"#fff",margin:"0 0 20px",lineHeight:1.2}}>{greet()},<br/>Driver</h1>
            <button onClick={isOnline ? handleGoOffline : handleGoOnline} style={{width:"100%",padding:"16px",background:isOnline?"#fff":"rgba(255,255,255,.15)",border:isOnline?"none":"1.5px solid rgba(255,255,255,.6)",borderRadius:100,fontSize:17,fontWeight:700,color:isOnline?GRN:"#fff",fontFamily:FD,cursor:"pointer",boxShadow:isOnline?"0 8px 32px rgba(0,0,0,.15)":"none",display:"flex",alignItems:"center",justifyContent:"center",gap:8}}>
              <span style={{width:10,height:10,borderRadius:"50%",background:isOnline?GRN:"rgba(255,255,255,.5)",display:"inline-block",flexShrink:0}}/>
              {isOnline ? "Go Offline" : "Start Drive"}
            </button>
          </div>
          <div style={{flex:1,padding:"20px 20px 0",overflowY:"auto",WebkitOverflowScrolling:"touch",background:t.bg}}>
            {isOnline && (
              <>
                <h3 style={{fontFamily:FD,fontSize:18,fontWeight:700,color:t.text,margin:"0 0 12px"}}>Ride Requests</h3>
                {pendingRides.length === 0 ? (
                  <div style={{background:t.card,borderRadius:14,padding:"20px 16px",textAlign:"center",marginBottom:20}}>
                    <p style={{fontSize:14,color:t.t2,margin:0,fontFamily:FN}}>Looking for passengers nearby…</p>
                  </div>
                ) : pendingRides.map((r,i)=>(
                  <div key={r.id||i} style={{background:t.card,borderRadius:14,padding:"16px",marginBottom:12,border:`1.5px solid ${t.brd}`}}>
                    <div style={{display:"flex",alignItems:"flex-start",gap:12,marginBottom:12}}>
                      <div style={{width:40,height:40,borderRadius:"50%",background:BBG,display:"flex",alignItems:"center",justifyContent:"center",flexShrink:0}}>{I.usr(B,20)}</div>
                      <div style={{flex:1,minWidth:0}}>
                        <p style={{fontWeight:700,fontSize:15,color:t.text,margin:"0 0 2px",fontFamily:FD}}>{r.riderName||"Passenger"}</p>
                        <p style={{fontSize:12,color:t.t2,margin:0,fontFamily:FN,overflow:"hidden",textOverflow:"ellipsis",whiteSpace:"nowrap"}}>{r.pickupAddress||"Campus pickup"} → {r.destinationAddress||"Destination"}</p>
                      </div>
                      <span style={{background:BBG,color:B,fontWeight:700,fontSize:14,padding:"6px 12px",borderRadius:100,flexShrink:0}}>${(r.price||6.50).toFixed(2)}</span>
                    </div>
                    <div style={{display:"flex",gap:10}}>
                      <button onClick={()=>handleAcceptRide(r)} style={{flex:1,padding:"11px",background:GRN,border:"none",borderRadius:10,fontSize:14,fontWeight:700,color:"#fff",fontFamily:FN,cursor:"pointer"}}>Accept</button>
                      <button onClick={()=>setPendingRides(prev=>prev.filter((_,j)=>j!==i))} style={{flex:1,padding:"11px",background:"rgba(239,68,68,.08)",border:`1.5px solid ${RED}`,borderRadius:10,fontSize:14,fontWeight:700,color:RED,fontFamily:FN,cursor:"pointer"}}>Skip</button>
                    </div>
                  </div>
                ))}
              </>
            )}
            <h3 style={{fontFamily:FD,fontSize:18,fontWeight:700,color:t.text,margin:"0 0 12px"}}>Recent Drives</h3>
            {(driverRides ?? RIDES_D).map((r,i)=>(
              <div key={i} style={{background:t.card,borderRadius:14,padding:"14px 16px",display:"flex",alignItems:"center",gap:12,marginBottom:10}}>
                <div style={{width:40,height:40,borderRadius:"50%",background:BBG,display:"flex",alignItems:"center",justifyContent:"center",flexShrink:0}}>{I.pin(B)}</div>
                <div style={{flex:1}}>
                  <p style={{fontWeight:600,fontSize:15,color:t.text,margin:0}}>{r.d}</p>
                  <p style={{fontSize:12,color:t.t2,margin:"2px 0 0"}}>{r.dt}</p>
                </div>
                <span style={{background:"rgba(34,197,94,.15)",color:GRN,fontWeight:700,fontSize:14,padding:"6px 14px",borderRadius:100}}>+ ${r.e.toFixed(2)}</span>
              </div>
            ))}
            <div style={{height:20}}/>
          </div>
          <div style={{flexShrink:0}}><Nav/></div>
        </div>
      )}
      {/* Search overlay */}
      {showSrch&&(
        <div style={{position:"absolute",inset:0,background:t.bg,zIndex:100,display:"flex",flexDirection:"column"}}>
          <div style={{padding:"16px 20px",display:"flex",alignItems:"center",gap:12}}>
            <button onClick={closeSearch} style={{background:"none",border:"none",cursor:"pointer",padding:0}}>{I.bk(t.text)}</button>
            <input autoFocus value={sq}
              onChange={e=>{
                const v=e.target.value; setSq(v);
                clearTimeout(srchDebounce.current);
                if(v.length<2){setSuggestions([]);setSrchLoading(false);return;}
                setSrchLoading(true);
                srchDebounce.current=setTimeout(async()=>{
                  try{const r=await apiAutocomplete(v);setSuggestions(r.data?.predictions||[]);}
                  catch{setSuggestions([]);}
                  finally{setSrchLoading(false);}
                },350);
              }}
              placeholder="Where are you going?" style={inp({flex:1})}/>
          </div>
          <div style={{flex:1,overflow:"auto",padding:"0 20px",minHeight:0}}>
            {suggestions.length>0 ? (
              <>
                <p style={{fontSize:12,fontWeight:600,color:t.t3,marginBottom:12,textTransform:"uppercase",letterSpacing:".05em"}}>Suggestions</p>
                {suggestions.map(p=>(
                  <button key={p.place_id} onClick={()=>selectDestination(p.structured_formatting?.main_text||p.description)} style={{width:"100%",padding:"14px 0",background:"none",border:"none",borderBottom:`1px solid ${t.brd}`,cursor:"pointer",textAlign:"left",fontFamily:FN,display:"flex",alignItems:"center",gap:14}}>
                    <div style={{width:36,height:36,borderRadius:10,background:BBG,display:"flex",alignItems:"center",justifyContent:"center",flexShrink:0}}>{I.pin(B,16)}</div>
                    <div style={{flex:1,minWidth:0}}>
                      <p style={{fontWeight:600,fontSize:15,color:t.text,margin:0,overflow:"hidden",textOverflow:"ellipsis",whiteSpace:"nowrap"}}>{p.structured_formatting?.main_text||p.description}</p>
                      {p.structured_formatting?.secondary_text&&<p style={{fontSize:12,color:t.t2,margin:"2px 0 0",overflow:"hidden",textOverflow:"ellipsis",whiteSpace:"nowrap"}}>{p.structured_formatting.secondary_text}</p>}
                    </div>
                  </button>
                ))}
              </>
            ) : srchLoading ? (
              <p style={{color:t.t3,fontSize:14,textAlign:"center",marginTop:32}}>Searching…</p>
            ) : (
              <>
                <p style={{fontSize:12,fontWeight:600,color:t.t3,marginBottom:12,textTransform:"uppercase",letterSpacing:".05em"}}>Popular on Campus</p>
                {PLACES.filter(d=>d.toLowerCase().includes(sq.toLowerCase())).map(d=>(
                  <button key={d} onClick={()=>selectDestination(d)} style={{width:"100%",padding:"16px 0",background:"none",border:"none",borderBottom:`1px solid ${t.brd}`,cursor:"pointer",textAlign:"left",fontFamily:FN,display:"flex",alignItems:"center",gap:14}}>
                    <div style={{width:36,height:36,borderRadius:10,background:BBG,display:"flex",alignItems:"center",justifyContent:"center"}}>{I.pin(B,16)}</div>
                    <span style={{fontWeight:500,fontSize:15,color:t.text}}>{d}</span>
                  </button>
                ))}
              </>
            )}
          </div>
        </div>
      )}
    </S>
  );

  // ═══════════════════════════════════════════
  // 4b. DRIVER ACTIVE RIDE
  // ═══════════════════════════════════════════
  const DriverActiveRide = () => (
    <S n="driverActiveRide" scr={scr}>
      <div style={{flex:1,display:"flex",flexDirection:"column",background:t.bg}}>
        <div style={{background:`linear-gradient(135deg,${B},${BD})`,color:"#fff",padding:"40px 24px 20px"}}>
          <h1 style={{fontFamily:FD,fontSize:26,fontWeight:800,margin:"0 0 4px"}}>Active Ride</h1>
          <p style={{margin:0,fontSize:13,opacity:.8}}>
            {driverRidePhase==="ACCEPTED"?"Head to pickup location":driverRidePhase==="DRIVER_ARRIVED"?"Waiting for passenger":"Trip in progress"}
          </p>
        </div>
        <div style={{flex:1,overflow:"auto",padding:"20px",minHeight:0}}>
          <div style={{background:t.card,borderRadius:18,padding:20,marginBottom:16}}>
            <div style={{display:"flex",alignItems:"center",gap:14,marginBottom:14}}>
              <div style={{width:52,height:52,borderRadius:"50%",background:BBG,display:"flex",alignItems:"center",justifyContent:"center",flexShrink:0}}>{I.usr(B,26)}</div>
              <div style={{flex:1}}>
                <p style={{fontWeight:700,fontSize:17,color:t.text,margin:"0 0 2px",fontFamily:FD}}>{driverRide?.riderName||"Passenger"}</p>
                <p style={{fontSize:13,color:t.t2,margin:0,fontFamily:FN}}>Ride #{(driverRide?.id||"").slice(-6)||"—"}</p>
              </div>
              <span style={{background:BBG,color:B,fontWeight:800,fontSize:18,padding:"8px 16px",borderRadius:100,flexShrink:0,fontFamily:FD}}>${(driverRide?.driverEarnings||driverRide?.price||0).toFixed(2)}</span>
            </div>
            <div style={{background:t.bg,borderRadius:12,padding:"12px 14px"}}>
              <div style={{display:"flex",alignItems:"flex-start",gap:10,marginBottom:10}}>
                <div style={{width:10,height:10,borderRadius:"50%",background:GRN,marginTop:3,flexShrink:0}}/>
                <div>
                  <p style={{fontSize:11,color:t.t3,margin:"0 0 1px",fontFamily:FN,textTransform:"uppercase",letterSpacing:".04em"}}>Pickup</p>
                  <p style={{fontSize:14,fontWeight:600,color:t.text,margin:0,fontFamily:FN}}>{driverRide?.pickupAddress||"—"}</p>
                </div>
              </div>
              <div style={{width:1,height:12,background:t.brd,marginLeft:4,marginBottom:10}}/>
              <div style={{display:"flex",alignItems:"flex-start",gap:10}}>
                {I.pin(B,14)}
                <div>
                  <p style={{fontSize:11,color:t.t3,margin:"0 0 1px",fontFamily:FN,textTransform:"uppercase",letterSpacing:".04em"}}>Destination</p>
                  <p style={{fontSize:14,fontWeight:600,color:t.text,margin:0,fontFamily:FN}}>{driverRide?.destinationAddress||"—"}</p>
                </div>
              </div>
            </div>
          </div>
          {driverRidePhase==="ACCEPTED"&&(
            <button onClick={handleDriverArrived} style={{width:"100%",padding:"16px",background:B,border:"none",borderRadius:14,fontSize:16,fontWeight:700,color:"#fff",fontFamily:FD,cursor:"pointer",marginBottom:12}}>
              I've Arrived at Pickup
            </button>
          )}
          {driverRidePhase==="DRIVER_ARRIVED"&&(
            <button onClick={handleDriverStartTrip} style={{width:"100%",padding:"16px",background:GRN,border:"none",borderRadius:14,fontSize:16,fontWeight:700,color:"#fff",fontFamily:FD,cursor:"pointer",marginBottom:12}}>
              Start Trip
            </button>
          )}
          {driverRidePhase==="IN_PROGRESS"&&(
            <button onClick={handleDriverCompleteTrip} style={{width:"100%",padding:"16px",background:GRN,border:"none",borderRadius:14,fontSize:16,fontWeight:700,color:"#fff",fontFamily:FD,cursor:"pointer",marginBottom:12}}>
              Complete Trip
            </button>
          )}
          <button onClick={async()=>{
            try{if(driverRide)await cancelRide(driverRide.id,userId,"Driver cancelled");}catch{}
            setDriverRide(null);setDriverRidePhase("");
            if(isOnline){pendingPollRef.current=setInterval(async()=>{try{const r=await getPendingRides();setPendingRides(r.data?.data||[]);}catch{}},5000);}
            go("home","rides");
          }} style={{width:"100%",padding:"14px",background:"none",border:`1.5px solid ${RED}`,borderRadius:14,fontSize:15,fontWeight:600,color:RED,fontFamily:FN,cursor:"pointer"}}>
            Cancel Ride
          </button>
        </div>
      </div>
    </S>
  );

  // ═══════════════════════════════════════════
  // 5. SCHEDULE AHEAD (Rider)
  // ═══════════════════════════════════════════
  const SchedWeek = () => {
    const days=["S","M","T","W","T","F","S"];
    return (
      <S n="schedWeek" scr={scr}>
        <div style={{height:"100%",display:"flex",flexDirection:"column",overflow:"hidden",background:t.bg}}>
          <div style={{flexShrink:0,padding:"40px 24px 0"}}>
            <h1 style={{fontFamily:FD,fontSize:28,fontWeight:800,color:t.text,margin:"0 0 16px"}}>Schedule Ahead</h1>
          </div>
          <div style={{flex:1,overflowY:"auto",WebkitOverflowScrolling:"touch",padding:"0 20px"}}>
            <div style={{background:`linear-gradient(145deg,${BL},${B})`,borderRadius:18,padding:20,marginBottom:20,color:"#fff"}}>
              <div style={{display:"flex",alignItems:"center",justifyContent:"space-between",marginBottom:16}}>
                <button onClick={prevMonth} style={{background:"none",border:"none",cursor:"pointer",padding:4,lineHeight:0}}>{I.bk("#fff",22)}</button>
                <h3 style={{fontFamily:FD,fontSize:18,fontWeight:700,margin:0}}>{MONTH_NAMES[calMonth]} {calYear}</h3>
                <button onClick={nextMonth} style={{background:"none",border:"none",cursor:"pointer",padding:4,lineHeight:0}}>{I.arr("#fff",22)}</button>
              </div>
              <div style={{display:"grid",gridTemplateColumns:"repeat(7,1fr)",gap:4,textAlign:"center"}}>
                {days.map((d,i)=><span key={i} style={{fontSize:12,fontWeight:700,opacity:.7,padding:"6px 0"}}>{d}</span>)}
                {buildCalWeeks(calYear,calMonth).flat().map((d,i)=>(
                  <button key={i} onClick={()=>d&&!isPastDay(d)&&setCalDay(d)} style={{background:d===calDay?"rgba(255,255,255,.3)":"none",border:"none",borderRadius:8,padding:"8px 0",color:d===0?"transparent":isPastDay(d)?"rgba(255,255,255,.3)":"#fff",fontWeight:d&&!isPastDay(d)?700:400,fontSize:14,cursor:d&&!isPastDay(d)?"pointer":"default",fontFamily:FN}}>{d||""}</button>
                ))}
              </div>
            </div>
            <label style={{fontSize:14,fontWeight:700,color:t.text,display:"block",marginBottom:8}}>Arrival Time on Campus</label>
            <input type="time" value={aTime} onChange={e=>setATime(e.target.value)} style={inp({marginBottom:20})}/>
            <label style={{fontSize:14,fontWeight:700,color:t.text,display:"block",marginBottom:8}}>Leaving From</label>
            <input placeholder="Enter starting location" value={lFrom} onChange={e=>setLFrom(e.target.value)} style={inp({marginBottom:20})}/>
            <label style={{fontSize:14,fontWeight:700,color:t.text,display:"block",marginBottom:8}}>Going To</label>
            <input placeholder="Enter campus destination" value={gTo} onChange={e=>setGTo(e.target.value)} style={inp({marginBottom:24})}/>
            {calError&&<p style={{color:RED,fontSize:13,fontWeight:600,margin:"0 0 12px",textAlign:"center"}}>{calError}</p>}
            {calSuccess&&<p style={{color:GRN,fontSize:13,fontWeight:600,margin:"0 0 12px",textAlign:"center"}}>{calSuccess}</p>}
            <button onClick={handlePostSchedule} disabled={schedLoading} style={{width:"100%",padding:"16px",background:schedLoading?"#9CA3AF":B,border:"none",borderRadius:14,fontSize:16,fontWeight:700,color:"#fff",fontFamily:FD,cursor:schedLoading?"not-allowed":"pointer",marginBottom:28}}>
              {schedLoading?"Posting…":"Post Schedule"}
            </button>
            {/* ── Available Matches ── */}
            {(matchLoading || schedMatches.length > 0 || lastPostedSchedId) && (
              <>
                <h3 style={{fontFamily:FD,fontSize:18,fontWeight:700,color:t.text,margin:"0 0 12px"}}>Available Matches</h3>
                {matchSuccess ? (
                  <div style={{background:"rgba(34,197,94,.1)",border:`1.5px solid ${GRN}`,borderRadius:14,padding:"16px 18px",marginBottom:16,display:"flex",alignItems:"center",gap:12}}>
                    <span style={{fontSize:20}}>✅</span>
                    <p style={{fontWeight:600,fontSize:14,color:GRN,margin:0,fontFamily:FN}}>{matchSuccess}</p>
                  </div>
                ) : matchLoading ? (
                  [0,1].map(i=>(
                    <div key={i} style={{background:t.card,borderRadius:14,padding:"14px 16px",marginBottom:10,opacity:.5}}>
                      <div style={{height:14,borderRadius:6,background:t.brd,marginBottom:8,width:"50%"}}/>
                      <div style={{height:11,borderRadius:6,background:t.brd,width:"65%"}}/>
                    </div>
                  ))
                ) : schedMatches.length === 0 ? (
                  <div style={{background:t.card,borderRadius:14,padding:"16px 18px",marginBottom:16,textAlign:"center"}}>
                    <p style={{fontSize:14,color:t.t2,margin:0,fontFamily:FN}}>No matching rides yet. We'll notify you when a match is available.</p>
                  </div>
                ) : schedMatches.map((m,i)=>(
                  <div key={i} style={{background:t.card,borderRadius:14,padding:"16px",marginBottom:12,border:`1.5px solid ${t.brd}`}}>
                    <div style={{display:"flex",alignItems:"center",gap:12,marginBottom:12}}>
                      <div style={{width:44,height:44,borderRadius:"50%",background:BBG,display:"flex",alignItems:"center",justifyContent:"center",flexShrink:0}}>{I.usr(B,22)}</div>
                      <div style={{flex:1,minWidth:0}}>
                        <p style={{fontWeight:700,fontSize:15,color:t.text,margin:"0 0 2px",fontFamily:FD}}>{m.userName}</p>
                        <p style={{fontSize:12,color:t.t2,margin:0,fontFamily:FN}}>{m.arrivalTime ? formatArrivalTime(m.arrivalTime) : ""} • {m.timeDiffMinutes === 0 ? "same time" : `${m.timeDiffMinutes} min apart`}</p>
                      </div>
                    </div>
                    <div style={{background:t.bg,borderRadius:10,padding:"10px 12px",marginBottom:12}}>
                      <p style={{fontSize:12,color:t.t3,margin:"0 0 4px",fontFamily:FN,textTransform:"uppercase",letterSpacing:".04em"}}>Route</p>
                      <p style={{fontSize:13,fontWeight:600,color:t.text,margin:0,fontFamily:FN,overflow:"hidden",textOverflow:"ellipsis",whiteSpace:"nowrap"}}>{m.leavingFrom} → {m.goingTo}</p>
                    </div>
                    <div style={{display:"flex",gap:10}}>
                      <button onClick={async()=>{
                        try {
                          await apiAcceptMatch(lastPostedSchedId, m.scheduleId);
                          setMatchSuccess(`Ride matched! You'll meet ${m.userName} at ${m.arrivalTime ? formatArrivalTime(m.arrivalTime) : "your scheduled time"}.`);
                          setSchedMatches([]);
                        } catch { /* ignore */ }
                      }} style={{flex:1,padding:"11px",background:GRN,border:"none",borderRadius:10,fontSize:14,fontWeight:700,color:"#fff",fontFamily:FN,cursor:"pointer"}}>Accept</button>
                      <button onClick={()=>setSchedMatches(prev=>prev.filter((_,j)=>j!==i))} style={{flex:1,padding:"11px",background:"rgba(239,68,68,.08)",border:`1.5px solid ${RED}`,borderRadius:10,fontSize:14,fontWeight:700,color:RED,fontFamily:FN,cursor:"pointer"}}>Decline</button>
                    </div>
                  </div>
                ))}
              </>
            )}

            <h3 style={{fontFamily:FD,fontSize:18,fontWeight:700,color:t.text,margin:"0 0 12px"}}>My Scheduled Rides</h3>
            {riderSchedsLoading ? (
              [0,1,2].map(i=>(
                <div key={i} style={{background:t.card,borderRadius:14,padding:"14px 16px",marginBottom:10,opacity:.5}}>
                  <div style={{height:14,borderRadius:6,background:t.brd,marginBottom:8,width:"55%"}}/>
                  <div style={{height:11,borderRadius:6,background:t.brd,width:"40%"}}/>
                </div>
              ))
            ) : riderSchedules.length===0 ? (
              <p style={{color:t.t3,fontSize:14,textAlign:"center",padding:"16px 0"}}>No rides scheduled yet.</p>
            ) : riderSchedules.map((s,i)=>(
              <div key={i} style={{background:t.card,borderRadius:14,padding:"14px 16px",display:"flex",alignItems:"center",gap:12,marginBottom:10}}>
                <div style={{width:40,height:40,borderRadius:"50%",background:BBG,display:"flex",alignItems:"center",justifyContent:"center",flexShrink:0}}>{I.cal(B,18)}</div>
                <div style={{flex:1,minWidth:0}}>
                  <p style={{fontWeight:700,fontSize:15,color:t.text,margin:"0 0 3px",fontFamily:FD,overflow:"hidden",textOverflow:"ellipsis",whiteSpace:"nowrap"}}>
                    {s.date} • {s.arrivalTime ? formatArrivalTime(s.arrivalTime) : ""}
                  </p>
                  <p style={{fontSize:12,color:t.t2,margin:0,fontFamily:FN,overflow:"hidden",textOverflow:"ellipsis",whiteSpace:"nowrap"}}>
                    {s.leavingFrom} → {s.goingTo}
                  </p>
                </div>
                <button onClick={async()=>{const id=s.id;setRiderSchedules(prev=>prev.filter((_,j)=>j!==i));if(id){try{await deleteSchedule(id,userId);}catch{}}}} style={{background:"none",border:"none",cursor:"pointer",padding:4,flexShrink:0}}>{I.xc()}</button>
              </div>
            ))}
            <div style={{height:20}}/>
          </div>
          <div style={{flexShrink:0}}><Nav/></div>
        </div>
      </S>
    );
  };

  // ═══════════════════════════════════════════
  // 6. DRIVER CALENDAR SCHEDULE
  // ═══════════════════════════════════════════
  const CalSched = () => {
    const days=["S","M","T","W","T","F","S"];
    return (
      <S n="driverCal" scr={scr}>
        <div style={{height:"100%",display:"flex",flexDirection:"column",overflow:"hidden",background:t.bg}}>
          <div style={{flexShrink:0,padding:"40px 24px 0"}}>
            <h1 style={{fontFamily:FD,fontSize:28,fontWeight:800,color:t.text,margin:"0 0 16px"}}>{drv?"Driver Schedule":"Schedule Ahead"}</h1>
          </div>
          <div style={{flex:1,overflowY:"auto",WebkitOverflowScrolling:"touch",padding:"0 20px"}}>
            <div style={{background:`linear-gradient(145deg,${BL},${B})`,borderRadius:18,padding:20,marginBottom:24,color:"#fff"}}>
              <div style={{display:"flex",alignItems:"center",justifyContent:"space-between",marginBottom:16}}>
                <button onClick={prevMonth} style={{background:"none",border:"none",cursor:"pointer",padding:4,lineHeight:0}}>{I.bk("#fff",22)}</button>
                <h3 style={{fontFamily:FD,fontSize:18,fontWeight:700,margin:0}}>{MONTH_NAMES[calMonth]} {calYear}</h3>
                <button onClick={nextMonth} style={{background:"none",border:"none",cursor:"pointer",padding:4,lineHeight:0}}>{I.arr("#fff",22)}</button>
              </div>
              <div style={{display:"grid",gridTemplateColumns:"repeat(7,1fr)",gap:4,textAlign:"center"}}>
                {days.map((d,i)=><span key={i} style={{fontSize:12,fontWeight:700,opacity:.7,padding:"6px 0"}}>{d}</span>)}
                {buildCalWeeks(calYear,calMonth).flat().map((d,i)=>(
                  <button key={i} onClick={()=>d&&!isPastDay(d)&&setCalDay(d)} style={{background:d===calDay?"rgba(255,255,255,.3)":"none",border:"none",borderRadius:8,padding:"8px 0",color:d===0?"transparent":isPastDay(d)?"rgba(255,255,255,.3)":"#fff",fontWeight:d&&!isPastDay(d)?700:400,fontSize:14,cursor:d&&!isPastDay(d)?"pointer":"default",fontFamily:FN}}>{d||""}</button>
                ))}
              </div>
            </div>
            <label style={{fontSize:14,fontWeight:700,color:t.text,display:"block",marginBottom:8}}>Arrival Time on Campus</label>
            <input type="time" value={aTime} onChange={e=>setATime(e.target.value)} style={inp({marginBottom:20})}/>
            <label style={{fontSize:14,fontWeight:700,color:t.text,display:"block",marginBottom:8}}>Leaving From</label>
            <input placeholder="Enter starting location" value={lFrom} onChange={e=>setLFrom(e.target.value)} style={inp({marginBottom:20})}/>
            <label style={{fontSize:14,fontWeight:700,color:t.text,display:"block",marginBottom:8}}>Going To</label>
            <input placeholder="Enter campus destination" value={gTo} onChange={e=>setGTo(e.target.value)} style={inp({marginBottom:24})}/>
            {calError&&<p style={{color:RED,fontSize:13,fontWeight:600,margin:"0 0 12px",textAlign:"center"}}>{calError}</p>}
            {calSuccess&&<p style={{color:GRN,fontSize:13,fontWeight:600,margin:"0 0 12px",textAlign:"center"}}>{calSuccess}</p>}
            <button onClick={handlePostSchedule} disabled={schedLoading} style={{width:"100%",padding:"16px",background:schedLoading?"#9CA3AF":B,border:"none",borderRadius:14,fontSize:16,fontWeight:700,color:"#fff",fontFamily:FD,cursor:schedLoading?"not-allowed":"pointer",marginBottom:20}}>
              {schedLoading?"Posting…":(drv?"Post Availability":"Post Schedule")}
            </button>
          </div>
          <div style={{flexShrink:0}}><Nav/></div>
        </div>
      </S>
    );
  };

  // ═══════════════════════════════════════════
  // 7. ACCOUNT
  // ═══════════════════════════════════════════
  const Account = () => (
    <S n="account" scr={scr}>
      <div style={{height:"100%",display:"flex",flexDirection:"column",overflow:"hidden",background:t.bg,position:"relative"}}>
        <div style={{flexShrink:0,background:`linear-gradient(135deg,${B},${BD})`,color:"#fff",padding:"40px 24px 36px",display:"flex",alignItems:"flex-start"}}>
          <h1 style={{fontFamily:FD,fontSize:26,fontWeight:800,margin:0,flex:1}}>Account</h1>
          <div style={{position:"relative"}}>
            <div style={{width:72,height:72,borderRadius:"50%",background:dk?"#334155":"#E2E8F0",display:"flex",alignItems:"center",justifyContent:"center",border:"3px solid #fff"}}>{I.usr(dk?"#94A3B8":B,36)}</div>
            <div style={{position:"absolute",bottom:-4,right:-4}}>{I.edt()}</div>
          </div>
        </div>
        <div style={{flex:1,overflowY:"auto",WebkitOverflowScrolling:"touch",padding:"20px 24px 0"}}>
          <label style={{fontSize:13,fontWeight:500,color:t.t2,display:"block",marginBottom:8}}>Account Type</label>
          <div style={{display:"flex",background:t.card,borderRadius:12,overflow:"hidden",marginBottom:24,opacity:accountLoading?.6:1,pointerEvents:accountLoading?"none":"auto"}}>
            <button onClick={()=>handleSwitchAccountType(false)} style={{flex:1,padding:14,border:"none",fontSize:15,fontWeight:600,fontFamily:FN,cursor:"pointer",background:!drv?B:"transparent",color:!drv?"#fff":t.t2,borderRadius:12,transition:"all .25s"}}>Passenger</button>
            <button onClick={()=>handleSwitchAccountType(true)} style={{flex:1,padding:14,border:"none",fontSize:15,fontWeight:600,fontFamily:FN,cursor:"pointer",background:drv?B:"transparent",color:drv?"#fff":t.t2,borderRadius:12,transition:"all .25s"}}>Driver</button>
          </div>

          <div style={{display:"flex",alignItems:"center",justifyContent:"space-between",marginBottom:12}}>
            <h3 style={{fontFamily:FD,fontSize:16,fontWeight:700,color:t.text,margin:0}}>Personal Information</h3>
            {editMode ? (
              <button onClick={handleSaveProfile} disabled={profSaving} style={{background:B,border:"none",borderRadius:8,padding:"6px 14px",color:"#fff",fontSize:13,fontWeight:700,fontFamily:FN,cursor:profSaving?"not-allowed":"pointer"}}>{profSaving?"Saving…":"Save"}</button>
            ) : (
              <span onClick={handleEditProfile} style={{color:B,fontSize:14,fontWeight:600,cursor:"pointer"}}>Edit</span>
            )}
          </div>

          <label style={{fontSize:12,color:t.t2,display:"block",marginBottom:6}}>Name</label>
          <div style={{display:"flex",gap:12,marginBottom:16}}>
            <input value={editMode?editProf.f:prof.f} readOnly={!editMode} onChange={e=>editMode&&setEditProf(p=>({...p,f:e.target.value}))} style={inp()}/>
            <input value={editMode?editProf.l:prof.l} readOnly={!editMode} onChange={e=>editMode&&setEditProf(p=>({...p,l:e.target.value}))} style={inp()}/>
          </div>
          <label style={{fontSize:12,color:t.t2,display:"block",marginBottom:6}}>Email</label>
          <input value={prof.em} readOnly style={inp({marginBottom:16,opacity:.65})}/>
          <label style={{fontSize:12,color:t.t2,display:"block",marginBottom:6}}>Phone Number</label>
          <input value={editMode?editProf.ph:prof.ph} readOnly={!editMode} onChange={e=>editMode&&setEditProf(p=>({...p,ph:e.target.value}))} style={inp({marginBottom:20})}/>

          <button onClick={()=>{setPwModal(true);setPwError("");setPwSuccess("");setCurrentPw("");setNewPw("");}} style={{width:"100%",padding:"14px 16px",background:t.card,border:"none",borderRadius:12,fontSize:15,fontWeight:600,color:t.text,fontFamily:FN,cursor:"pointer",textAlign:"left",marginBottom:10}}>Change Password</button>
          <button style={{width:"100%",padding:"14px 16px",background:t.card,border:"none",borderRadius:12,fontSize:15,fontWeight:600,color:t.t2,fontFamily:FN,cursor:"default",textAlign:"left",marginBottom:10}}>Payment Methods <span style={{fontSize:12,opacity:.6}}>(Coming Soon)</span></button>
          <div onClick={()=>setDk(!dk)} style={{width:"100%",padding:"14px 16px",background:t.card,borderRadius:12,cursor:"pointer",display:"flex",alignItems:"center",justifyContent:"space-between",marginBottom:12,boxSizing:"border-box"}}>
            <span style={{fontSize:15,fontWeight:600,color:t.text,fontFamily:FN,display:"flex",alignItems:"center",gap:10}}>{dk?"☀️":"🌙"} Dark Mode</span>
            <div style={{width:48,height:28,borderRadius:14,background:dk?B:"#CBD5E1",transition:"background .25s",display:"flex",alignItems:"center",padding:"0 3px",boxSizing:"border-box",flexShrink:0}}>
              <div style={{width:22,height:22,borderRadius:"50%",background:"#fff",transition:"transform .25s",transform:dk?"translateX(20px)":"translateX(0)",boxShadow:"0 1px 4px rgba(0,0,0,.2)"}}/>
            </div>
          </div>
          <button onClick={handleSignOut} style={{width:"100%",padding:"14px",background:RED,border:"none",borderRadius:12,fontSize:15,fontWeight:600,color:"#fff",fontFamily:FN,cursor:"pointer",display:"flex",alignItems:"center",justifyContent:"center",gap:8}}>{I.out()} Sign Out</button>
          <div style={{height:20}}/>
        </div>
        <div style={{flexShrink:0}}><Nav/></div>
        {pwModal&&(
          <div style={{position:"absolute",inset:0,background:"rgba(0,0,0,.5)",zIndex:50,display:"flex",alignItems:"flex-end"}}>
            <div style={{background:t.bg,borderRadius:"24px 24px 0 0",padding:"24px 24px 32px",width:"100%",boxSizing:"border-box"}}>
              <div style={{width:40,height:4,borderRadius:2,background:t.brd,margin:"0 auto 20px"}}/>
              <h3 style={{fontFamily:FD,fontSize:20,fontWeight:800,color:t.text,margin:"0 0 20px"}}>Change Password</h3>
              <label style={{fontSize:12,color:t.t2,display:"block",marginBottom:6}}>Current Password</label>
              <input type="password" value={currentPw} onChange={e=>setCurrentPw(e.target.value)} placeholder="Enter current password" style={inp({marginBottom:14})}/>
              <label style={{fontSize:12,color:t.t2,display:"block",marginBottom:6}}>New Password</label>
              <input type="password" value={newPw} onChange={e=>setNewPw(e.target.value)} placeholder="6+ characters" style={inp({marginBottom:14})}/>
              {pwError&&<p style={{color:RED,fontSize:13,fontWeight:600,margin:"0 0 12px"}}>{pwError}</p>}
              {pwSuccess&&<p style={{color:GRN,fontSize:13,fontWeight:600,margin:"0 0 12px"}}>{pwSuccess}</p>}
              <div style={{display:"flex",gap:12}}>
                <button onClick={()=>setPwModal(false)} style={{flex:1,padding:"14px",background:t.card,border:"none",borderRadius:12,fontSize:15,fontWeight:600,color:t.t2,fontFamily:FN,cursor:"pointer"}}>Cancel</button>
                <button onClick={handleChangePassword} disabled={pwLoading} style={{flex:1,padding:"14px",background:pwLoading?"#9CA3AF":B,border:"none",borderRadius:12,fontSize:15,fontWeight:700,color:"#fff",fontFamily:FN,cursor:pwLoading?"not-allowed":"pointer"}}>{pwLoading?"Saving…":"Update"}</button>
              </div>
            </div>
          </div>
        )}
      </div>
    </S>
  );

  // ═══════════════════════════════════════════
  // 8. RIDE PREVIEW
  // ═══════════════════════════════════════════
  const RidePreview = () => (
    <S n="ridePreview" scr={scr}>
      <div style={{flex:1,display:"flex",flexDirection:"column",background:t.bg}}>
        <div style={{background:`linear-gradient(135deg,${B},${BD})`,color:"#fff",padding:"40px 24px 20px"}}>
          <button onClick={()=>go("home","rides")} style={{background:"none",border:"none",cursor:"pointer",padding:0,marginBottom:12}}>{I.bk("#fff",26)}</button>
          <h1 style={{fontFamily:FD,fontSize:26,fontWeight:800,margin:0}}>Your Ride</h1>
        </div>
        <div style={{flex:1,overflow:"auto",padding:"24px 20px",minHeight:0}}>
          <div style={{background:t.card,borderRadius:18,padding:20,marginBottom:20}}>
            <div style={{display:"flex",alignItems:"center",gap:12,marginBottom:14}}>
              <div style={{width:10,height:10,borderRadius:"50%",background:GRN,flexShrink:0}}/>
              <span style={{fontSize:14,color:t.t2,fontFamily:FN}}>Current Location</span>
            </div>
            <div style={{width:1.5,height:18,background:t.brd,marginLeft:4,marginBottom:14}}/>
            <div style={{display:"flex",alignItems:"center",gap:12}}>
              {I.pin(B,16)}
              <span style={{fontSize:15,fontWeight:600,color:t.text,fontFamily:FN,flex:1,overflow:"hidden",textOverflow:"ellipsis",whiteSpace:"nowrap"}}>{selectedDest?.address}</span>
            </div>
          </div>
          <div style={{background:`linear-gradient(135deg,${B},${BD})`,borderRadius:18,padding:20,marginBottom:20,color:"#fff",display:"flex",justifyContent:"space-between",alignItems:"center"}}>
            <div>
              <p style={{margin:"0 0 4px",fontSize:12,opacity:.75,fontFamily:FN}}>Estimated fare</p>
              <p style={{margin:0,fontFamily:FD,fontSize:34,fontWeight:800}}>${(ridePrice||6.50).toFixed(2)}</p>
              <p style={{margin:"8px 0 0",fontSize:11,opacity:.6,fontFamily:FN}}>$3.00 base + $1.50/mile</p>
            </div>
            <div style={{textAlign:"right"}}>
              <p style={{margin:"0 0 4px",fontSize:12,opacity:.75,fontFamily:FN}}>ETA</p>
              <p style={{margin:0,fontFamily:FD,fontSize:26,fontWeight:700}}>{rideEta} min</p>
            </div>
          </div>
          <div style={{background:t.card,borderRadius:14,padding:"14px 18px",display:"flex",alignItems:"center",gap:14,marginBottom:24}}>
            <div style={{width:44,height:44,borderRadius:12,background:BBG,display:"flex",alignItems:"center",justifyContent:"center",flexShrink:0}}>{I.car(B,22)}</div>
            <div style={{flex:1}}>
              <p style={{fontWeight:700,fontSize:15,color:t.text,margin:0,fontFamily:FD}}>UniRide Standard</p>
              <p style={{fontSize:12,color:t.t2,margin:"2px 0 0",fontFamily:FN}}>Nearby driver • 4.8 ★</p>
            </div>
          </div>
          {requestError&&<p style={{color:RED,fontSize:13,fontWeight:600,margin:"0 0 12px",textAlign:"center"}}>{requestError}</p>}
          <button onClick={handleRequestRide} disabled={requestLoading||!userId} style={{width:"100%",padding:"18px",background:requestLoading||!userId?"#9CA3AF":B,border:"none",borderRadius:14,fontSize:18,fontWeight:700,color:"#fff",fontFamily:FD,cursor:requestLoading||!userId?"not-allowed":"pointer"}}>
            {requestLoading?"Requesting…":"Request UniRide"}
          </button>
          {!userId&&<p style={{color:t.t3,fontSize:12,textAlign:"center",marginTop:8,fontFamily:FN}}>Sign in to request a ride</p>}
        </div>
      </div>
    </S>
  );

  // ═══════════════════════════════════════════
  // 9. FINDING DRIVER
  // ═══════════════════════════════════════════
  const FindingDriver = () => (
    <S n="findingDriver" scr={scr}>
      <div style={{flex:1,display:"flex",flexDirection:"column",alignItems:"center",justifyContent:"center",background:t.bg,padding:"40px 32px"}}>
        <style>{`@keyframes ur-spin{to{transform:rotate(360deg)}}`}</style>
        <div style={{position:"relative",width:120,height:120,marginBottom:36}}>
          <div style={{position:"absolute",inset:0,borderRadius:"50%",border:`4px solid ${BBG}`}}/>
          <div style={{position:"absolute",inset:0,borderRadius:"50%",border:"4px solid transparent",borderTopColor:B,animation:"ur-spin 1s linear infinite"}}/>
          <div style={{position:"absolute",inset:10,borderRadius:"50%",background:B,display:"flex",alignItems:"center",justifyContent:"center",boxShadow:`0 8px 32px ${B}55`}}>
            {I.car("#fff",38)}
          </div>
        </div>
        <h2 style={{fontFamily:FD,fontSize:26,fontWeight:800,color:t.text,margin:"0 0 8px",textAlign:"center"}}>Finding your driver…</h2>
        <p style={{color:t.t2,fontSize:15,margin:"0 0 6px",textAlign:"center",fontFamily:FN,overflow:"hidden",textOverflow:"ellipsis",maxWidth:"100%"}}>to {selectedDest?.address}</p>
        <div style={{background:t.card,borderRadius:12,padding:"10px 24px",marginTop:8,marginBottom:40}}>
          <span style={{fontFamily:FD,fontSize:22,fontWeight:700,color:t.text}}>${(ridePrice||6.50).toFixed(2)}</span>
          <span style={{color:t.t2,fontSize:13,fontFamily:FN,marginLeft:8}}>estimated fare</span>
        </div>
        <button onClick={handleCancelRide} style={{padding:"12px 40px",background:"none",border:`1.5px solid ${t.brd}`,borderRadius:100,fontSize:15,fontWeight:600,color:t.t2,fontFamily:FN,cursor:"pointer"}}>Cancel</button>
      </div>
    </S>
  );

  // ═══════════════════════════════════════════
  // 10. IN-RIDE
  // ═══════════════════════════════════════════
  const InRide = () => (
    <S n="inRide" scr={scr}>
      <div style={{flex:1,display:"flex",flexDirection:"column",background:t.bg,position:"relative"}}>
        <div style={{height:"42%",minHeight:220,position:"relative",overflow:"hidden"}}>
          <MapView
            dk={dk}
            driverPos={driverPos}
            pickup={userLoc}
            destination={selectedDest?.lat ? { lat: selectedDest.lat, lng: selectedDest.lng } : null}
          />
          <button onClick={()=>setSosOpen(true)} style={{position:"absolute",top:16,right:16,width:50,height:50,borderRadius:"50%",background:RED,border:"none",cursor:"pointer",display:"flex",alignItems:"center",justifyContent:"center",boxShadow:`0 4px 16px rgba(239,68,68,.5)`,zIndex:10}}>
            <span style={{color:"#fff",fontWeight:800,fontSize:12,fontFamily:FD,letterSpacing:".02em"}}>SOS</span>
          </button>
        </div>
        <div style={{flex:1,background:t.bg,borderRadius:"24px 24px 0 0",marginTop:-20,padding:"20px 20px 16px",overflow:"auto",minHeight:0,boxShadow:"0 -8px 32px rgba(0,0,0,.08)"}}>
          <div style={{display:"flex",alignItems:"center",gap:14,marginBottom:18,paddingBottom:18,borderBottom:`1px solid ${t.brd}`}}>
            <div style={{width:52,height:52,borderRadius:"50%",background:BBG,display:"flex",alignItems:"center",justifyContent:"center",flexShrink:0}}>{I.usr(B,26)}</div>
            <div style={{flex:1}}>
              <p style={{fontWeight:700,fontSize:17,color:t.text,margin:0,fontFamily:FD}}>{rideDriver.name}</p>
              <p style={{fontSize:13,color:t.t2,margin:"2px 0 0",fontFamily:FN}}>{driverArrived?"Driver arrived ✓":"En route to you"} • ★ 4.9</p>
            </div>
            <div style={{background:BBG,borderRadius:12,padding:"8px 14px",textAlign:"center",flexShrink:0}}>
              <p style={{fontFamily:FD,fontSize:22,fontWeight:800,color:B,margin:0}}>{formatTimer(tripSeconds)}</p>
              <p style={{fontSize:10,color:t.t2,margin:"2px 0 0",fontFamily:FN,textTransform:"uppercase",letterSpacing:".05em"}}>trip time</p>
            </div>
          </div>
          <div style={{display:"flex",alignItems:"center",gap:12,marginBottom:18}}>
            {I.pin(B)}
            <div style={{flex:1,minWidth:0}}>
              <p style={{fontSize:11,color:t.t3,margin:"0 0 2px",fontFamily:FN,textTransform:"uppercase",letterSpacing:".05em"}}>Destination</p>
              <p style={{fontSize:15,fontWeight:600,color:t.text,margin:0,fontFamily:FN,overflow:"hidden",textOverflow:"ellipsis",whiteSpace:"nowrap"}}>{selectedDest?.address}</p>
            </div>
            <div style={{background:t.card,borderRadius:10,padding:"6px 12px",textAlign:"center",flexShrink:0}}>
              <p style={{fontSize:11,color:t.t3,margin:"0 0 2px",fontFamily:FN}}>ETA</p>
              <p style={{fontWeight:700,fontSize:14,color:t.text,margin:0,fontFamily:FD}}>{Math.max(1,rideEta-Math.floor(tripSeconds/60))} min</p>
            </div>
          </div>
          <div style={{display:"flex",gap:12,marginBottom:10}}>
            <button onClick={()=>{navigator.share?.({title:"UniRide Trip",text:`I'm in a UniRide headed to ${selectedDest?.address}`}).catch(()=>{});}} style={{flex:1,padding:"13px",background:BBG,border:"none",borderRadius:14,fontSize:14,fontWeight:600,color:B,fontFamily:FN,cursor:"pointer"}}>Share Trip</button>
            <button onClick={handleCancelRide} disabled={driverArrived} style={{flex:1,padding:"13px",background:driverArrived?t.card:t.card,border:`1.5px solid ${driverArrived?t.brd:t.brd}`,borderRadius:14,fontSize:14,fontWeight:600,color:driverArrived?t.t3:t.text,fontFamily:FN,cursor:driverArrived?"not-allowed":"pointer",opacity:driverArrived?.45:1}}>Cancel</button>
          </div>
          <button onClick={handleCompleteTrip} style={{width:"100%",padding:"14px",background:GRN,border:"none",borderRadius:14,fontSize:15,fontWeight:700,color:"#fff",fontFamily:FD,cursor:"pointer"}}>Complete Trip</button>
        </div>
        {sosOpen&&(
          <div style={{position:"absolute",inset:0,background:"rgba(0,0,0,.6)",zIndex:50,display:"flex",flexDirection:"column",justifyContent:"flex-end"}}>
            <div style={{background:t.bg,borderRadius:"24px 24px 0 0",padding:"24px 24px 32px"}}>
              <div style={{width:40,height:4,borderRadius:2,background:t.brd,margin:"0 auto 20px"}}/>
              <h3 style={{fontFamily:FD,fontSize:20,fontWeight:800,color:RED,margin:"0 0 20px",textAlign:"center"}}>Emergency Options</h3>
              <a href="tel:2255783231" style={{display:"flex",alignItems:"center",gap:14,padding:"16px 18px",background:"rgba(239,68,68,.07)",border:`1.5px solid ${RED}`,borderRadius:14,marginBottom:12,textDecoration:"none"}}>
                <div style={{width:44,height:44,borderRadius:12,background:RED,display:"flex",alignItems:"center",justifyContent:"center",flexShrink:0}}>
                  <svg width="22" height="22" viewBox="0 0 24 24" fill="none"><path d="M22 16.92v3a2 2 0 01-2.18 2 19.79 19.79 0 01-8.63-3.07A19.5 19.5 0 015 12.21 19.79 19.79 0 011.93 3.58 2 2 0 013.92 2h3a2 2 0 012 1.72c.127.96.361 1.903.7 2.81a2 2 0 01-.45 2.11L8.09 9.91a16 16 0 006 6l1.27-1.27a2 2 0 012.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0122 16.92z" stroke="#fff" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/></svg>
                </div>
                <div>
                  <p style={{fontWeight:700,fontSize:15,color:RED,margin:0,fontFamily:FD}}>Call LSU Police</p>
                  <p style={{fontSize:12,color:t.t2,margin:"2px 0 0",fontFamily:FN}}>225-578-3231 • Campus emergency</p>
                </div>
              </a>
              <button onClick={()=>{navigator.share?.({title:"UniRide SOS",text:`I'm in a UniRide to ${selectedDest?.address||"campus"}. Please track my trip.`}).catch(()=>{});setSosOpen(false);}} style={{width:"100%",display:"flex",alignItems:"center",gap:14,padding:"16px 18px",background:t.card,border:"none",borderRadius:14,marginBottom:20,cursor:"pointer",textAlign:"left"}}>
                <div style={{width:44,height:44,borderRadius:12,background:B,display:"flex",alignItems:"center",justifyContent:"center",flexShrink:0}}>
                  <svg width="22" height="22" viewBox="0 0 24 24" fill="none"><path d="M4 12v8a2 2 0 002 2h12a2 2 0 002-2v-8M16 6l-4-4-4 4M12 2v13" stroke="#fff" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/></svg>
                </div>
                <div>
                  <p style={{fontWeight:700,fontSize:15,color:t.text,margin:0,fontFamily:FD}}>Share My Trip</p>
                  <p style={{fontSize:12,color:t.t2,margin:"2px 0 0",fontFamily:FN}}>Send live trip info to a contact</p>
                </div>
              </button>
              <button onClick={()=>setSosOpen(false)} style={{width:"100%",padding:"14px",background:t.card,border:`1px solid ${t.brd}`,borderRadius:14,fontSize:15,fontWeight:600,color:t.t2,fontFamily:FN,cursor:"pointer"}}>Dismiss</button>
            </div>
          </div>
        )}
      </div>
    </S>
  );

  // ═══════════════════════════════════════════
  // 11. RATE RIDE
  // ═══════════════════════════════════════════
  const RateRide = () => (
    <S n="rateRide" scr={scr}>
      <div style={{flex:1,display:"flex",flexDirection:"column",alignItems:"center",justifyContent:"center",background:t.bg,padding:"40px 32px"}}>
        <div style={{width:90,height:90,borderRadius:"50%",background:BBG,display:"flex",alignItems:"center",justifyContent:"center",marginBottom:24,boxShadow:`0 8px 32px ${B}22`}}>
          {I.car(B,44)}
        </div>
        <h2 style={{fontFamily:FD,fontSize:26,fontWeight:800,color:t.text,margin:"0 0 6px",textAlign:"center"}}>You've arrived!</h2>
        <p style={{color:t.t2,fontSize:14,margin:"0 0 4px",textAlign:"center",fontFamily:FN}}>at {selectedDest?.address}</p>
        <p style={{color:t.t2,fontSize:14,margin:"0 0 28px",textAlign:"center",fontFamily:FN}}>How was your ride with <strong style={{color:t.text}}>{rideDriver.name}</strong>?</p>
        <div style={{display:"flex",gap:8,marginBottom:32}}>
          {[1,2,3,4,5].map(n=>(
            <button key={n} onClick={()=>setStarRating(n)} style={{background:"none",border:"none",cursor:"pointer",padding:"4px 6px",fontSize:42,color:n<=starRating?"#F59E0B":"#D1D5DB",lineHeight:1,filter:n<=starRating?"drop-shadow(0 2px 6px rgba(245,158,11,.4))":"none"}}>★</button>
          ))}
        </div>
        <div style={{width:"100%",background:t.card,borderRadius:14,padding:"14px 18px",marginBottom:24,display:"flex",justifyContent:"space-between",alignItems:"center"}}>
          <span style={{fontSize:14,color:t.t2,fontFamily:FN}}>Total fare</span>
          <span style={{fontWeight:700,fontSize:18,color:t.text,fontFamily:FD}}>${(ridePrice||6.50).toFixed(2)}</span>
        </div>
        <button onClick={handleRateRide} disabled={!starRating||rateLoading} style={{width:"100%",padding:"16px",background:!starRating||rateLoading?"#9CA3AF":B,border:"none",borderRadius:14,fontSize:17,fontWeight:700,color:"#fff",fontFamily:FD,cursor:!starRating||rateLoading?"not-allowed":"pointer",marginBottom:12}}>
          {rateLoading?"Submitting…":"Submit Rating"}
        </button>
        <button onClick={()=>{setRideId("");setRidePrice(null);setStarRating(0);setTripSeconds(0);go("home","rides");}} style={{background:"none",border:"none",cursor:"pointer",color:t.t2,fontSize:14,fontFamily:FN,padding:8}}>Skip</button>
      </div>
    </S>
  );

  return (
    <div style={{minHeight:"100vh",background:"linear-gradient(135deg,#0F172A,#1E293B)",display:"flex",alignItems:"center",justifyContent:"center",padding:"12px 0"}}>
      <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Outfit:wght@400;500;600;700;800&display=swap" rel="stylesheet"/>
      <div style={{fontFamily:FN,width:"100%",maxWidth:400,margin:"0 auto",height:"100dvh",maxHeight:880,background:t.bg,position:"relative",overflow:"hidden",borderRadius:28,boxShadow:"0 25px 80px rgba(0,0,0,.25)"}}>
        {Welcome()}{Register()}{Login()}{VerifyEmail()}{RiderHome()}{SchedWeek()}{CalSched()}{Account()}
        {RidePreview()}{FindingDriver()}{InRide()}{RateRide()}{DriverActiveRide()}
      </div>
    </div>
  );
}
