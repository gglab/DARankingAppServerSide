import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Trip } from './trip.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Trip>;

@Injectable()
export class TripService {

    private resourceUrl =  SERVER_API_URL + 'api/trips';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(trip: Trip): Observable<EntityResponseType> {
        const copy = this.convert(trip);
        return this.http.post<Trip>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(trip: Trip): Observable<EntityResponseType> {
        const copy = this.convert(trip);
        return this.http.put<Trip>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Trip>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Trip[]>> {
        const options = createRequestOption(req);
        return this.http.get<Trip[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Trip[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Trip = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Trip[]>): HttpResponse<Trip[]> {
        const jsonResponse: Trip[] = res.body;
        const body: Trip[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Trip.
     */
    private convertItemFromServer(trip: Trip): Trip {
        const copy: Trip = Object.assign({}, trip);
        copy.start = this.dateUtils
            .convertDateTimeFromServer(trip.start);
        return copy;
    }

    /**
     * Convert a Trip to a JSON which can be sent to the server.
     */
    private convert(trip: Trip): Trip {
        const copy: Trip = Object.assign({}, trip);

        copy.start = this.dateUtils.toDate(trip.start);
        return copy;
    }
}
