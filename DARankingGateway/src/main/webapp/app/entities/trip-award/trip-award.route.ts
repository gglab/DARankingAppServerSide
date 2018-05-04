import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { TripAwardComponent } from './trip-award.component';
import { TripAwardDetailComponent } from './trip-award-detail.component';
import { TripAwardPopupComponent } from './trip-award-dialog.component';
import { TripAwardDeletePopupComponent } from './trip-award-delete-dialog.component';

export const tripAwardRoute: Routes = [
    {
        path: 'trip-award',
        component: TripAwardComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TripAwards'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'trip-award/:id',
        component: TripAwardDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TripAwards'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const tripAwardPopupRoute: Routes = [
    {
        path: 'trip-award-new',
        component: TripAwardPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TripAwards'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'trip-award/:id/edit',
        component: TripAwardPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TripAwards'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'trip-award/:id/delete',
        component: TripAwardDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TripAwards'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
