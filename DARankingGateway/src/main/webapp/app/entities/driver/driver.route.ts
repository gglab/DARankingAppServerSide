import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { DriverComponent } from './driver.component';
import { DriverDetailComponent } from './driver-detail.component';
import { DriverPopupComponent } from './driver-dialog.component';
import { DriverDeletePopupComponent } from './driver-delete-dialog.component';

export const driverRoute: Routes = [
    {
        path: 'driver',
        component: DriverComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Drivers'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'driver/:id',
        component: DriverDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Drivers'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const driverPopupRoute: Routes = [
    {
        path: 'driver-new',
        component: DriverPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Drivers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'driver/:id/edit',
        component: DriverPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Drivers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'driver/:id/delete',
        component: DriverDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Drivers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
