import "./App.css";
import useRouteElements from "./useRouteElements";

function App() {
  const useRoutes = useRouteElements();

  return <>{useRoutes}</>;
}

export default App;