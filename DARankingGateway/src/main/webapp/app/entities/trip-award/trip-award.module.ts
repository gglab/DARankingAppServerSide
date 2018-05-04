import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DaRankingGatewaySharedModule } from '../../shared';
import {
    TripAwardService,
    TripAwardPopupService,
    TripAwardComponent,
    TripAwardDetailComponent,
    TripAwardDialogComponent,
    TripAwardPopupComponent,
    TripAwardDeletePopupComponent,
    TripAwardDeleteDialogComponent,
    tripAwardRoute,
    tripAwardPopupRoute,
} from './';

const ENTITY_STATES = [
    ...tripAwardRoute,
    ...tripAwardPopupRoute,
];

@NgModule({
    imports: [
        DaRankingGatewaySharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TripAwardComponent,
        TripAwardDetailComponent,
        TripAwardDialogComponent,
        TripAwardDeleteDialogComponent,
        TripAwardPopupComponent,
        TripAwardDeletePopupComponent,
    ],
    entryComponents: [
        TripAwardComponent,
        TripAwardDialogComponent,
        TripAwardPopupComponent,
        TripAwardDeleteDialogComponent,
        TripAwardDeletePopupComponent,
    ],
    providers: [
        TripAwardService,
        TripAwardPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DaRankingGatewayTripAwardModule {}
