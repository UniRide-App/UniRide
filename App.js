import { useEffect, useState } from 'react'
import UniRide from './UniRide'
import SplashScreen from './components/SplashScreen'

function App() {
  const [appReady, setAppReady] = useState(false)
  const [showSplash, setShowSplash] = useState(true)

  useEffect(() => {
    const splashPreview = new URLSearchParams(window.location.search).get('splash') === '1'
    if (splashPreview) return
    let cancelled = false
    const start = Date.now()
    const MIN_SPLASH_MS = 1800
    const finish = () => {
    const elapsed = Date.now() - start
    const remaining = Math.max(0, MIN_SPLASH_MS - elapsed)
      window.setTimeout(() => {
        if (!cancelled) setAppReady(true)
      }, remaining)
    }

    if (document.readyState === 'complete') finish()
    else window.addEventListener('load', finish, { once: true })

    return () => {
      cancelled = true
      window.removeEventListener('load', finish)
    }
  }, [])

  if (showSplash) {
    return (
    <SplashScreen
        appReady={appReady}
        onFinished={() => {
          setShowSplash(false)
        }}
      />
    )
  }

  return <UniRide />
}
export default App