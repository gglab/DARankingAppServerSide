import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { TripComponent } from './trip.component';
import { TripDetailComponent } from './trip-detail.component';
import { TripPopupComponent } from './trip-dialog.component';
import { TripDeletePopupComponent } from './trip-delete-dialog.component';

export const tripRoute: Routes = [
    {
        path: 'trip',
        component: TripComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Trips'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'trip/:id',
        component: TripDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Trips'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const tripPopupRoute: Routes = [
    {
        path: 'trip-new',
        component: TripPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Trips'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'trip/:id/edit',
        component: TripPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Trips'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'trip/:id/delete',
        component: TripDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Trips'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
