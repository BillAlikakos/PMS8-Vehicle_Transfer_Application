import * as React from 'react';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import ListSubheader from '@mui/material/ListSubheader';
import DashboardIcon from '@mui/icons-material/Dashboard';
import SellIcon from '@mui/icons-material/Sell';
import List from '@mui/material/List';
import Collapse from '@mui/material/Collapse';
import InventoryIcon from '@mui/icons-material/Inventory';
import ExpandLess from '@mui/icons-material/ExpandLess';
import ExpandMore from '@mui/icons-material/ExpandMore';
import StarBorder from '@mui/icons-material/StarBorder';
import PersonIcon from '@mui/icons-material/Person';
import { useState } from 'react';
import { useNavigate } from "react-router-dom";
import { useEffect } from 'react';

const MainListItems = ({open}) => {
    const [sellerOpen, setSelleterOpen] = useState(false);
    const [buyerOpen, setBuyerOpen] = useState(false)
    const navigate = useNavigate()
    console.log('open', open, sellerOpen, buyerOpen)

    useEffect(() => {
        if (!open) {
            setSelleterOpen(false);
            setBuyerOpen(false)
        }

    },[open])
    return (
    <React.Fragment>
            <ListItemButton onClick={() => navigate('/main')}>
            <ListItemIcon>
                <DashboardIcon />
            </ListItemIcon>
            <ListItemText primary="Αρχική Σελίδα" />
            </ListItemButton>
            <ListItemButton onClick={() => {
                navigate('/sell-procedure')
            }}>
            <ListItemIcon>
                    <PersonIcon />
            </ListItemIcon>
            <ListItemText primary="Πώληση" />
                {sellerOpen && open ? <ExpandLess onClick={(e) => {
                    setSelleterOpen(!sellerOpen)
                    e.stopPropagation()
                }} /> :
                    <ExpandMore onClick={(e) => {
                        setSelleterOpen(!sellerOpen)
                        e.stopPropagation()

                    }} />}
        </ListItemButton>
            <Collapse in={sellerOpen && open} timeout="auto" unmountOnExit>
                <List component="div" disablePadding>
                    <ListItemButton sx={{ pl: 4 }} onClick={() => {
                        navigate('/availableCars')
                    }}>
                    <ListItemIcon>
                        <StarBorder />
                    </ListItemIcon>
                    <ListItemText primary="Διαθέσιμα Οχήματα Χρήστη" />
                    </ListItemButton>
                    <ListItemButton sx={{ pl: 4 }} onClick={() => {
                        navigate('/sellerTransferCar')
                    }}>
                        <ListItemIcon>
                            <SellIcon />
                        </ListItemIcon>
                        <ListItemText primary="Μεταβίβαση Οχήματος" />
                    </ListItemButton>
            </List>
        </Collapse>
            <ListItemButton onClick={() => {
                setBuyerOpen(!buyerOpen);              
                navigate('/buy-procedure');              
            }}>
            <ListItemIcon>
                    <PersonIcon />
            </ListItemIcon>
                <ListItemText primary="Αγορά" />
        </ListItemButton>
    </React.Fragment>
    )
}


export default MainListItems