import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IActividad, Actividad } from '../actividad.model';
import { ActividadService } from '../service/actividad.service';

@Injectable({ providedIn: 'root' })
export class ActividadRoutingResolveService implements Resolve<IActividad> {
  constructor(protected service: ActividadService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IActividad> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((actividad: HttpResponse<Actividad>) => {
          if (actividad.body) {
            return of(actividad.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Actividad());
  }
}
