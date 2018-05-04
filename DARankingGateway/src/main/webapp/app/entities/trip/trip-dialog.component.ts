import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Trip } from './trip.model';
import { TripPopupService } from './trip-popup.service';
import { TripService } from './trip.service';
import { Driver, DriverService } from '../driver';
import { TripAward, TripAwardService } from '../trip-award';

@Component({
    selector: 'jhi-trip-dialog',
    templateUrl: './trip-dialog.component.html'
})
export class TripDialogComponent implements OnInit {

    trip: Trip;
    isSaving: boolean;

    drivers: Driver[];

    tripawards: TripAward[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private tripService: TripService,
        private driverService: DriverService,
        private tripAwardService: TripAwardService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.driverService.query()
            .subscribe((res: HttpResponse<Driver[]>) => { this.drivers = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.tripAwardService.query()
            .subscribe((res: HttpResponse<TripAward[]>) => { this.tripawards = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.trip.id !== undefined) {
            this.subscribeToSaveResponse(
                this.tripService.update(this.trip));
        } else {
            this.subscribeToSaveResponse(
                this.tripService.create(this.trip));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Trip>>) {
        result.subscribe((res: HttpResponse<Trip>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Trip) {
        this.eventManager.broadcast({ name: 'tripListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackDriverById(index: number, item: Driver) {
        return item.id;
    }

    trackTripAwardById(index: number, item: TripAward) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-trip-popup',
    template: ''
})
export class TripPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tripPopupService: TripPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.tripPopupService
                    .open(TripDialogComponent as Component, params['id']);
            } else {
                this.tripPopupService
                    .open(TripDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
