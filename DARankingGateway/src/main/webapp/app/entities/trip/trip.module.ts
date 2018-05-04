import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DaRankingGatewaySharedModule } from '../../shared';
import {
    TripService,
    TripPopupService,
    TripComponent,
    TripDetailComponent,
    TripDialogComponent,
    TripPopupComponent,
    TripDeletePopupComponent,
    TripDeleteDialogComponent,
    tripRoute,
    tripPopupRoute,
} from './';

const ENTITY_STATES = [
    ...tripRoute,
    ...tripPopupRoute,
];

@NgModule({
    imports: [
        DaRankingGatewaySharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TripComponent,
        TripDetailComponent,
        TripDialogComponent,
        TripDeleteDialogComponent,
        TripPopupComponent,
        TripDeletePopupComponent,
    ],
    entryComponents: [
        TripComponent,
        TripDialogComponent,
        TripPopupComponent,
        TripDeleteDialogComponent,
        TripDeletePopupComponent,
    ],
    providers: [
        TripService,
        TripPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DaRankingGatewayTripModule {}
