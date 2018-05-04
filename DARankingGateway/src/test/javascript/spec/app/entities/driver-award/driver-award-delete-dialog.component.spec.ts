/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DaRankingGatewayTestModule } from '../../../test.module';
import { DriverAwardDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/driver-award/driver-award-delete-dialog.component';
import { DriverAwardService } from '../../../../../../main/webapp/app/entities/driver-award/driver-award.service';

describe('Component Tests', () => {

    describe('DriverAward Management Delete Component', () => {
        let comp: DriverAwardDeleteDialogComponent;
        let fixture: ComponentFixture<DriverAwardDeleteDialogComponent>;
        let service: DriverAwardService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DaRankingGatewayTestModule],
                declarations: [DriverAwardDeleteDialogComponent],
                providers: [
                    DriverAwardService
                ]
            })
            .overrideTemplate(DriverAwardDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DriverAwardDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DriverAwardService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
