import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { DriverAward } from './driver-award.model';
import { DriverAwardPopupService } from './driver-award-popup.service';
import { DriverAwardService } from './driver-award.service';
import { Driver, DriverService } from '../driver';

@Component({
    selector: 'jhi-driver-award-dialog',
    templateUrl: './driver-award-dialog.component.html'
})
export class DriverAwardDialogComponent implements OnInit {

    driverAward: DriverAward;
    isSaving: boolean;

    drivers: Driver[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private driverAwardService: DriverAwardService,
        private driverService: DriverService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.driverService.query()
            .subscribe((res: HttpResponse<Driver[]>) => { this.drivers = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.driverAward.id !== undefined) {
            this.subscribeToSaveResponse(
                this.driverAwardService.update(this.driverAward));
        } else {
            this.subscribeToSaveResponse(
                this.driverAwardService.create(this.driverAward));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<DriverAward>>) {
        result.subscribe((res: HttpResponse<DriverAward>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: DriverAward) {
        this.eventManager.broadcast({ name: 'driverAwardListModification', content: 'OK'});
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
    selector: 'jhi-driver-award-popup',
    template: ''
})
export class DriverAwardPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private driverAwardPopupService: DriverAwardPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.driverAwardPopupService
                    .open(DriverAwardDialogComponent as Component, params['id']);
            } else {
                this.driverAwardPopupService
                    .open(DriverAwardDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
