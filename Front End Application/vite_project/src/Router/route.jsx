import {
    createBrowserRouter,
} from "react-router-dom";
import Dashboard from "../Components/Dashboard";
import SellProcedure from "../Components/SellProcedure";
import AvailableCars from "../Components/AvailableCarsForSale";
import SellerTransferCar from "../Components/SellerTransferCar";
import SellCarForm from "../Components/SellCarForm";
import LogIn from "../Components/LogIn";
import Driven from '../Components/Driven';
import BuyProcedure from "../Components/BuyProcedure";

const router = createBrowserRouter([
    {
        path: '/login',
        element: <LogIn />
    },
    {
        path: '/driven',
        element : <Driven/>
    },
    {
        path: "/main",
        element: <Dashboard />,
    },
    {
        path: "/sell-procedure",
        element: <SellProcedure />,
    },
    {
        path: "/buy-procedure",
        element: <BuyProcedure />,
    },
    {
        path: "/availableCars",
        element: <AvailableCars />,
    },
    {
        path: "/sellerTransferCar",
        element: <SellerTransferCar />,
    },
    {
        path: "/sellCarForm",
        element: <SellCarForm />
    },
]);

export default router;