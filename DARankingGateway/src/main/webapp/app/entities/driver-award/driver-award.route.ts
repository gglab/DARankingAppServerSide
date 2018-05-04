import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { DriverAwardComponent } from './driver-award.component';
import { DriverAwardDetailComponent } from './driver-award-detail.component';
import { DriverAwardPopupComponent } from './driver-award-dialog.component';
import { DriverAwardDeletePopupComponent } from './driver-award-delete-dialog.component';

export const driverAwardRoute: Routes = [
    {
        path: 'driver-award',
        component: DriverAwardComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DriverAwards'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'driver-award/:id',
        component: DriverAwardDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DriverAwards'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const driverAwardPopupRoute: Routes = [
    {
        path: 'driver-award-new',
        component: DriverAwardPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DriverAwards'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'driver-award/:id/edit',
        component: DriverAwardPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DriverAwards'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'driver-award/:id/delete',
        component: DriverAwardDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DriverAwards'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
